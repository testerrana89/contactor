import {
  useGetContactsQuery,
  useGetFilteredContactsQuery,
} from "@contact-hub/services";

export const useSearch = (name: string) => {
  const { data: allContacts, isLoading } = useGetContactsQuery("contacts");
  const { data: filteredContacts } = useGetFilteredContactsQuery(
    { name },
    { skip: name.length < 3 }
  );

  const contacts = name.length >= 3 ? filteredContacts : allContacts;

  return { contacts, isLoading };
};

import { ContactDetails } from "@contact-hub/components";
import { useGetContactDetailsQuery } from "@contact-hub/services";
import { Container } from "@mui/material";
import { useRouter } from "next/router";

export default function EditContactPage() {
  const { query } = useRouter();
  const contactId = query["id"] as string;
  const { data: contact } = useGetContactDetailsQuery(contactId, {
    skip: !contactId,
  });
  return (
    <Container maxWidth="lg">
      {contact && <ContactDetails contact={contact} />}
    </Container>
  );
}

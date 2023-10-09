import { ContactForm, ProtectedRoute } from "@contact-hub/components";
import { useGetContactDetailsQuery } from "@contact-hub/services";
import { useRouter } from "next/router";

export default function EditContactPage() {
  const { query } = useRouter();
  const contactId = query["id"] as string;

  const { data: contact } = useGetContactDetailsQuery(contactId, {
    skip: !contactId,
  });

  return (
    <ProtectedRoute>
      <ContactForm contact={contact} />
    </ProtectedRoute>
  );
}

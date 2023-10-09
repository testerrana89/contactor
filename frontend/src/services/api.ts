import { RootState } from "@contact-hub/store";
import { Contact, LoginRequest, LoginResponse } from "@contact-hub/store/types";
import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

// Define the base URL for your API
const baseURL = "http://localhost:8080/api/";

export const api = createApi({
  baseQuery: fetchBaseQuery({
    baseUrl: baseURL,
    prepareHeaders(headers, { getState, endpoint }) {
      const token = (getState() as RootState).auth.token;
      if (token && endpoint === "updateContact") {
        headers.set("Authorization", `Bearer ${token}`);
      }

      return headers;
    },
  }),
  endpoints: (builder) => ({
    getContacts: builder.query<Contact[], string>({
      query: () => "contacts",
    }),
    getContactDetails: builder.query<Contact, string>({
      query: (contactId) => `contacts/${contactId}`,
    }),
    createContact: builder.mutation<Contact, Partial<Contact>>({
      query: (newContact) => ({
        url: "contacts",
        method: "POST",
        body: newContact,
      }),
    }),
    updateContact: builder.mutation<
      Contact,
      { contactId: string; updatedContact: Partial<Contact> }
    >({
      query: ({ contactId, updatedContact }) => ({
        url: `contacts/${contactId}`,
        method: "PUT",
        body: updatedContact,
      }),
    }),
    getFilteredContacts: builder.query<Contact[], { name: string }>({
      query: ({ name }) => `contacts/filter?name=${name}`,
    }),
    login: builder.mutation<LoginResponse, LoginRequest>({
      query: (loginData) => ({
        url: "login",
        method: "POST",
        body: loginData,
      }),
    }),
  }),
});

export const {
  useGetContactsQuery,
  useGetContactDetailsQuery,
  useCreateContactMutation,
  useUpdateContactMutation,
  useGetFilteredContactsQuery,
  useLoginMutation,
} = api;

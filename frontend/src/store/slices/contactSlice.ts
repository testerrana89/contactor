import { createSlice } from "@reduxjs/toolkit";
import { Contact } from "../types/Contact";

export type ContactSlice = {
  contacts: Contact[];
};

const initialState: ContactSlice = {
  contacts: [],
};

const contactSlice = createSlice({
  name: "contact",
  initialState: initialState,
  reducers: {},
});

export default contactSlice.reducer;

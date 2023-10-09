/* eslint-disable no-undef */
import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { ContactForm } from "..";

jest.mock("../../services", () => ({
  useCreateContactMutation: jest.fn(),
  useUpdateContactMutation: jest.fn(),
}));

test("it should render ContactForm and handle form submission", async () => {
  const createNew = jest.fn();
  const update = jest.fn();

  require("../../services").useCreateContactMutation.mockReturnValue([
    createNew,
  ]);
  require("../../services").useUpdateContactMutation.mockReturnValue([update]);

  render(<ContactForm />);

  const nameInput = screen.getByLabelText("Name");
  const phoneInput = screen.getByLabelText("Mobile Number");
  const companyInput = screen.getByLabelText("Company");
  const emailInput = screen.getByLabelText("Email");
  const websiteInput = screen.getByLabelText("Website");
  const saveButton = await screen.findByText("Save");

  fireEvent.change(nameInput, { target: { value: "name" } });
  fireEvent.change(phoneInput, { target: { value: "1234567890" } });
  fireEvent.change(companyInput, { target: { value: "company" } });
  fireEvent.change(emailInput, { target: { value: "mail@mail.com" } });
  fireEvent.change(websiteInput, { target: { value: "web.com" } });

  fireEvent.click(saveButton);

  await waitFor(() => {
    expect(createNew).toHaveBeenCalledWith({
      name: "name",
      phone: "1234567890",
      companyName: "company",
      email: "mail@mail.com",
      website: "web.com",
    });
  });
});

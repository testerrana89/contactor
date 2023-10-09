/* eslint-disable no-undef */
import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import { ContactList } from "..";

jest.mock("next/router", () => ({
  useRouter: jest.fn(),
}));

jest.mock("src/hooks/useSearch", () => {
  return {
    useSearch: () => ({
      contacts: [], // Provide sample data for testing
      isLoading: false,
    }),
  };
});

test("it should render ContactList and handle button click", () => {
  const push = jest.fn();
  require("next/router").useRouter.mockReturnValue({ push });

  render(<ContactList />);

  const createNewButton = screen.getByText("Create New");

  fireEvent.click(createNewButton);

  expect(push).toHaveBeenCalledWith("/contacts/new");
});

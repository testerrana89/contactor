/* eslint-disable no-undef */
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";
import { contactData } from "@contact-hub/utils/mockTestData";
import { ContactDetails } from "..";

jest.mock("next/router", () => ({
  useRouter: jest.fn(),
}));

jest.mock("src/hooks/useAuth", () => {
  return {
    useAuth: () => ({
      isAuthenticated: false,
    }),
  };
});

test("should render contact card details", () => {
  const push = jest.fn();
  require("next/router").useRouter.mockReturnValue({ push });

  render(<ContactDetails contact={contactData} />);

  const name = screen.getByRole("heading", { name: contactData.name });
  const email = screen.getByRole("heading", { name: contactData.email });
  const company = screen.getByRole("heading", {
    name: contactData.companyName,
  });
  const phone = screen.getByRole("heading", { name: contactData.phone });
  const website = screen.getByRole("heading", { name: contactData.website });

  expect(name).toBeInTheDocument();
  expect(email).toBeInTheDocument();
  expect(company).toBeInTheDocument();
  expect(phone).toBeInTheDocument();
  expect(website).toBeInTheDocument();
});

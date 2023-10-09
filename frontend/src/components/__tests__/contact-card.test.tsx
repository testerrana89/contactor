/* eslint-disable no-undef */
import { fireEvent, render, screen } from "@testing-library/react";
import ContactCard from "../ContactCard";
import "@testing-library/jest-dom";
import { contactData } from "@contact-hub/utils/mockTestData";

jest.mock("next/router", () => ({
  useRouter: jest.fn(),
}));

const push = jest.fn();
require("next/router").useRouter.mockReturnValue({ push });

jest.mock("src/hooks/useAuth", () => {
  return {
    useAuth: () => ({
      isAuthenticated: true,
    }),
  };
});

describe("ContactCard", () => {
  it("it should render contact card data", () => {
    render(<ContactCard contact={contactData} />);

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

  it("is should trigger card actions", () => {
    render(<ContactCard contact={contactData} />);

    const editButton = screen.getByRole("button", {
      name: "Edit",
    });

    fireEvent.click(editButton);
    expect(push).toHaveBeenCalledWith(`contacts/edit/${contactData.id}`);
  });
});

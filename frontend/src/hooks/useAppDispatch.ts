import { useDispatch } from "react-redux";
import type { AppDispatch } from "@contact-hub/store";

export const useAppDispatch: () => AppDispatch = useDispatch;

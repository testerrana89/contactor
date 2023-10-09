import { useAppDispatch, useAppSelector } from ".";
import { updateToken } from "@contact-hub/store/slices/authSlice";
import { RootState } from "@contact-hub/store";

export const useAuth = () => {
  const dispatch = useAppDispatch();
  const { token } = useAppSelector((root: RootState) => root.auth);

  const updateAuthToken = (token: string | undefined) => {
    dispatch(updateToken(token));
  };

  return {
    updateAuthToken,
    isAuthenticated: !!token,
  };
};

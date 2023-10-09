import { PayloadAction, createSlice } from "@reduxjs/toolkit";

type AuthSlice = {
  token: string | undefined;
};

const initialState: AuthSlice = {
  token: undefined,
};

const authSlice = createSlice({
  name: "auth",
  initialState: initialState,
  reducers: {
    updateToken: (state, action: PayloadAction<string | undefined>) => {
      console.log(action.payload);
      state.token = action.payload;
    },
  },
});

export const { updateToken } = authSlice.actions;

export default authSlice.reducer;

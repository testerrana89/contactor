import { ConfigureStoreOptions, configureStore } from "@reduxjs/toolkit";
import contactSlice from "./slices/contactSlice";
import authSlice from "./slices/authSlice";
import { api } from "@contact-hub/services";

export const createStore = (
  options?: ConfigureStoreOptions["preloadedState"] | undefined
) =>
  configureStore({
    reducer: {
      [api.reducerPath]: api.reducer,
      contact: contactSlice,
      auth: authSlice,
    },
    middleware: (getdefaultMiddleware) =>
      getdefaultMiddleware().concat([api.middleware]),
    ...options,
  });

export const store = createStore();

// Infer the `RootState` and `AppDispatch` types from the store itself
export type RootState = ReturnType<typeof store.getState>;
// Inferred type: {posts: PostsState, comments: CommentsState, users: UsersState}
export type AppDispatch = typeof store.dispatch;

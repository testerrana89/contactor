import { Box, Button, Grid } from "@mui/material";
import { useForm } from "react-hook-form";
import { FormInputText } from ".";
import { LoginRequest } from "@contact-hub/store/types";

import { useLoginMutation } from "@contact-hub/services";
import { useEffect } from "react";
import { useAuth } from "@contact-hub/hooks";

export default function LoginForm() {
  const { control, handleSubmit } = useForm();

  const [login, { isLoading, data: loginResponse }] = useLoginMutation();
  const { updateAuthToken } = useAuth();

  useEffect(() => {
    updateAuthToken(loginResponse?.token);
  }, [loginResponse, updateAuthToken]);

  const onLogin = (value: any) => {
    const loginRequest: LoginRequest = {
      email: value.email,
      password: value.password,
    };
    login(loginRequest);
  };

  return (
    <Box>
      <Grid container spacing={2} marginTop={1}>
        <Grid item md={12} xs={12}>
          <FormInputText
            control={control}
            label="Email"
            name="email"
            value={""}
          />
        </Grid>
        <Grid item md={12} xs={12}>
          <FormInputText
            control={control}
            label="Password"
            name="password"
            value={""}
            type="password"
          />
        </Grid>
      </Grid>
      <Box mt={2}>
        <Button variant="contained" fullWidth onClick={handleSubmit(onLogin)}>
          {isLoading ? "Authenticating.." : "Login"}
        </Button>
      </Box>
    </Box>
  );
}

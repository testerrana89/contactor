import { Box, Button, Grid, Typography } from "@mui/material";
import { useForm } from "react-hook-form";
import { FormInputText } from ".";
import {
  useCreateContactMutation,
  useUpdateContactMutation,
} from "@contact-hub/services";
import { Contact } from "@contact-hub/store/types";
import { useEffect } from "react";

type Props = {
  contact?: Contact;
};

export default function ContactForm({ contact }: Props) {
  const { control, setValue, handleSubmit } = useForm();
  const [createNew] = useCreateContactMutation();
  const [update] = useUpdateContactMutation();

  const onCreate = (value: any) => {
    if (contact) {
      update({ contactId: `${contact.id}`, updatedContact: value });
    } else {
      createNew(value);
    }
  };

  useEffect(() => {
    if (contact) {
      setValue("name", contact.name);
      setValue("phone", contact.phone);
      setValue("companyName", contact.companyName);
      setValue("email", contact.email);
      setValue("website", contact.website);
    }
  }, [contact, setValue]);

  return (
    <Box>
      <Typography variant="h6" component="h6">
        Create New Contact
      </Typography>
      <Grid container spacing={2} mt={1}>
        <Grid item md={4} sm={6} xs={12}>
          <FormInputText
            control={control}
            label="Name"
            name="name"
            value={""}
          />
        </Grid>
        <Grid item md={4} sm={6} xs={12}>
          <FormInputText
            control={control}
            label="Mobile Number"
            name="phone"
            value={""}
          />
        </Grid>
        <Grid item md={4} sm={6} xs={12}>
          <FormInputText
            control={control}
            label="Company"
            name="companyName"
            value={""}
          />
        </Grid>
        <Grid item md={4} sm={6} xs={12}>
          <FormInputText
            control={control}
            label="Email"
            name="email"
            value={""}
          />
        </Grid>
        <Grid item md={4} sm={6} xs={12}>
          <FormInputText
            control={control}
            label="Website"
            name="website"
            value={""}
          />
        </Grid>
      </Grid>
      <Grid container spacing={2} mt={1}>
        <Grid item md={4}>
          <Button
            variant="contained"
            fullWidth
            onClick={handleSubmit(onCreate)}
          >
            Save
          </Button>
        </Grid>
      </Grid>
    </Box>
  );
}

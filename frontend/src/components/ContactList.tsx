import {
  Box,
  Button,
  Grid,
  InputAdornment,
  Stack,
  TextField,
} from "@mui/material";
import ContactCard from "./ContactCard";
import Search from "@mui/icons-material/Search";
import { useRouter } from "next/router";
import { useState } from "react";
import { useSearch } from "@contact-hub/hooks";

export default function ContactList() {
  const { push } = useRouter();
  const [searchText, setSearchText] = useState("");

  const { contacts } = useSearch(searchText);

  const onSearch = (event: any) => {
    setSearchText(event.target.value);
  };

  const onCreateNew = () => {
    push(`/contacts/new`);
  };
  return (
    <Box>
      <Grid container spacing={2}>
        <Grid item md={10} sm={8} xs={12}>
          <TextField
            fullWidth
            label=""
            size="small"
            placeholder="Search "
            id="outlined-size-normal"
            defaultValue=""
            onChange={onSearch}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <Search />
                </InputAdornment>
              ),
            }}
          />
        </Grid>
        <Grid item md={2} sm={4} xs={12}>
          <Button fullWidth variant="contained" onClick={onCreateNew}>
            Create New
          </Button>
        </Grid>
      </Grid>

      <Stack direction="column" gap={2} mt={2}>
        {contacts?.map((item, index) => {
          return <ContactCard key={index} contact={item} />;
        })}
      </Stack>
    </Box>
  );
}

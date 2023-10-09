import { useAuth } from "@contact-hub/hooks";
import { Contact } from "@contact-hub/store/types";
import {
  Button,
  Card,
  CardActionArea,
  CardActions,
  CardContent,
  Grid,
  Typography,
} from "@mui/material";
import { useRouter } from "next/router";

type Props = {
  contact: Contact;
};

export default function ContactCard({ contact }: Props) {
  const { push } = useRouter();

  const { isAuthenticated } = useAuth();

  const onEditClick = () => {
    push(`contacts/edit/${contact.id}`);
  };

  const onCardClick = () => {
    push(`contacts/${contact.id}`);
  };

  return (
    <Card>
      <CardActionArea onClick={onCardClick}>
        <CardContent>
          <Typography gutterBottom variant="h5" component="h5">
            {contact.name}
          </Typography>
          <Grid container>
            <Grid item md={3} sm={6} xs={12}>
              <Typography
                variant="body2"
                color="text.secondary"
                component="p"
                role="heading"
              >
                {contact.email}
              </Typography>
            </Grid>
            <Grid item md={3} sm={6} xs={12}>
              <Typography
                variant="body2"
                color="text.secondary"
                component="p"
                role="heading"
              >
                {contact.phone}
              </Typography>
            </Grid>
            <Grid item md={3} sm={6} xs={12}>
              <Typography
                variant="body2"
                color="text.secondary"
                component="p"
                role="heading"
              >
                {contact.companyName}
              </Typography>
            </Grid>
            <Grid item md={3} sm={6} xs={12}>
              <Typography
                variant="body2"
                color="text.secondary"
                component="p"
                role="heading"
              >
                {contact.website}
              </Typography>
            </Grid>
          </Grid>
        </CardContent>
      </CardActionArea>
      <CardActions>
        {isAuthenticated && (
          <Button size="small" color="primary" onClick={onEditClick}>
            Edit
          </Button>
        )}
      </CardActions>
    </Card>
  );
}

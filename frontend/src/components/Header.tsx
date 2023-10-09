import {
  AppBar,
  Button,
  Container,
  Dialog,
  DialogContent,
  DialogTitle,
  Toolbar,
  Typography,
} from "@mui/material";
import ContactMail from "@mui/icons-material/ContactMail";
import LoginForm from "./LoginForm";
import { useEffect, useState } from "react";
import { useAuth } from "@contact-hub/hooks";

export default function Header() {
  const [open, setOpen] = useState(false);
  const { isAuthenticated } = useAuth();

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  useEffect(() => {
    isAuthenticated && handleClose();
  }, [isAuthenticated]);

  return (
    <>
      <AppBar position="static">
        <Container maxWidth="lg">
          <Toolbar disableGutters>
            <ContactMail sx={{ display: { xs: "none", md: "flex" }, mr: 1 }} />
            <Typography variant="h6" noWrap sx={{ flexGrow: 1 }}>
              Contact Hub
            </Typography>
            {!isAuthenticated && (
              <Button color="inherit" onClick={handleClickOpen}>
                Login
              </Button>
            )}
          </Toolbar>
        </Container>
        <Dialog open={open} onClose={handleClose}>
          <DialogTitle>Login</DialogTitle>
          <DialogContent>
            <LoginForm />
          </DialogContent>
        </Dialog>
      </AppBar>
      {/* <LinearProgress /> */}
    </>
  );
}

import React from 'react';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import { styled } from '@mui/material/styles';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const StyledLink = styled(Button)(({ theme }) => ({
  color: 'white',
  textDecoration: 'none',
  marginLeft: theme.spacing(2),
}));

function Navbar() {
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Food Order App
        </Typography>
        {user ? (
          <>
            <StyledLink
              component={RouterLink}
              to="/menu"
            >
              Menu
            </StyledLink>
            <StyledLink
              component={RouterLink}
              to="/orders"
            >
              My Orders
            </StyledLink>
            <Button color="inherit" onClick={handleLogout}>
              Logout
            </Button>
          </>
        ) : (
          <>
            <StyledLink
              component={RouterLink}
              to="/login"
            >
              Login
            </StyledLink>
            <StyledLink
              component={RouterLink}
              to="/register"
            >
              Register
            </StyledLink>
          </>
        )}
      </Toolbar>
    </AppBar>
  );
}

export default Navbar;

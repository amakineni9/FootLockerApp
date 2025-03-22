import React, { useState, useEffect } from 'react';
import Container from '@mui/material/Container';
import Grid from '@mui/material/Grid';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardActions from '@mui/material/CardActions';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import TextField from '@mui/material/TextField';
import { styled } from '@mui/material/styles';
import { toast } from 'react-toastify';
import axios from 'axios';

const StyledContainer = styled(Container)(({ theme }) => ({
  paddingTop: theme.spacing(4),
  paddingBottom: theme.spacing(4),
}));

const StyledCard = styled(Card)(({ theme }) => ({
  height: '100%',
  display: 'flex',
  flexDirection: 'column',
  justifyContent: 'space-between',
}));

const StyledQuantityField = styled(TextField)(({ theme }) => ({
  width: 80,
}));

function Menu() {
  const [menuItems, setMenuItems] = useState([]);
  const [cart, setCart] = useState({});

  useEffect(() => {
    fetchMenuItems();
  }, []);

  const fetchMenuItems = async () => {
    try {
      const response = await axios.get('/api/menu-items');
      setMenuItems(response.data);
    } catch (error) {
      toast.error('Failed to fetch menu items');
    }
  };

  const handleQuantityChange = (itemId, quantity) => {
    setCart((prev) => ({
      ...prev,
      [itemId]: Math.max(0, parseInt(quantity) || 0),
    }));
  };

  const placeOrder = async () => {
    try {
      const items = Object.entries(cart)
        .filter(([_, quantity]) => quantity > 0)
        .map(([itemId, quantity]) => {
          const menuItem = menuItems.find((item) => item._id === itemId);
          return {
            menuItem: itemId,
            quantity,
            price: menuItem.price,
          };
        });

      if (items.length === 0) {
        toast.warn('Please add items to your cart');
        return;
      }

      const totalAmount = items.reduce(
        (sum, item) => sum + item.price * item.quantity,
        0
      );

      await axios.post('/api/orders', {
        items,
        totalAmount,
        status: 'pending',
      });

      toast.success('Order placed successfully!');
      setCart({});
    } catch (error) {
      toast.error(error.response?.data?.message || 'Failed to place order');
    }
  };

  return (
    <StyledContainer>
      <Grid container spacing={4}>
        {menuItems.map((item) => (
          <Grid item key={item._id} xs={12} sm={6} md={4}>
            <StyledCard>
              <CardContent>
                <Typography gutterBottom variant="h5" component="h2">
                  {item.name}
                </Typography>
                <Typography variant="body2" color="textSecondary" component="p">
                  {item.description}
                </Typography>
                <Typography variant="h6" color="primary">
                  ${item.price.toFixed(2)}
                </Typography>
              </CardContent>
              <CardActions>
                <StyledQuantityField
                  label="Quantity"
                  type="number"
                  value={cart[item._id] || 0}
                  onChange={(e) => handleQuantityChange(item._id, e.target.value)}
                  inputProps={{ min: 0 }}
                />
              </CardActions>
            </StyledCard>
          </Grid>
        ))}
      </Grid>
      <Button
        variant="contained"
        color="primary"
        sx={{ marginTop: 2 }}
        onClick={placeOrder}
      >
        Place Order (
        {Object.values(cart).reduce((sum, quantity) => sum + quantity, 0)} items)
      </Button>
    </StyledContainer>
  );
}

export default Menu;

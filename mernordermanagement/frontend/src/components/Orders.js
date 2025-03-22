import React, { useState, useEffect } from 'react';
import Container from '@mui/material/Container';
import Paper from '@mui/material/Paper';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import Dialog from '@mui/material/Dialog';
import DialogTitle from '@mui/material/DialogTitle';
import DialogContent from '@mui/material/DialogContent';
import DialogActions from '@mui/material/DialogActions';
import { styled } from '@mui/material/styles';
import { toast } from 'react-toastify';
import axios from 'axios';

const StyledContainer = styled(Container)(({ theme }) => ({
  paddingTop: theme.spacing(4),
  paddingBottom: theme.spacing(4),
}));

const StatusChip = styled('span')(({ theme, status }) => ({
  padding: theme.spacing(1),
  borderRadius: theme.spacing(0.5),
  display: 'inline-block',
  backgroundColor: (() => {
    switch (status) {
      case 'pending':
        return '#ffd700';
      case 'preparing':
        return '#87ceeb';
      case 'ready':
        return '#90ee90';
      case 'delivered':
        return '#98fb98';
      case 'cancelled':
        return '#ff6b6b';
      default:
        return '#gray';
    }
  })(),
}));

function Orders() {
  const [orders, setOrders] = useState([]);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [dialogOpen, setDialogOpen] = useState(false);

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const response = await axios.get('/api/orders');
      setOrders(response.data);
    } catch (error) {
      toast.error('Failed to fetch orders');
    }
  };

  const handleCancel = async (orderId) => {
    try {
      await axios.delete(`/api/orders/${orderId}`);
      toast.success('Order cancelled successfully');
      fetchOrders();
    } catch (error) {
      toast.error(error.response?.data?.message || 'Failed to cancel order');
    }
  };

  const handleViewDetails = (order) => {
    setSelectedOrder(order);
    setDialogOpen(true);
  };

  return (
    <StyledContainer>
      <Typography variant="h4" gutterBottom>
        My Orders
      </Typography>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Order ID</TableCell>
              <TableCell>Date</TableCell>
              <TableCell>Total Amount</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {orders.map((order) => (
              <TableRow key={order._id}>
                <TableCell>{order._id}</TableCell>
                <TableCell>
                  {new Date(order.createdAt).toLocaleDateString()}
                </TableCell>
                <TableCell>${order.totalAmount.toFixed(2)}</TableCell>
                <TableCell>
                  <StatusChip status={order.status}>
                    {order.status}
                  </StatusChip>
                </TableCell>
                <TableCell>
                  <Button
                    variant="outlined"
                    color="primary"
                    onClick={() => handleViewDetails(order)}
                    sx={{ marginRight: 1 }}
                  >
                    View Details
                  </Button>
                  {order.status === 'pending' && (
                    <Button
                      variant="outlined"
                      color="error"
                      onClick={() => handleCancel(order._id)}
                    >
                      Cancel
                    </Button>
                  )}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)}>
        <DialogTitle>Order Details</DialogTitle>
        <DialogContent>
          {selectedOrder && (
            <>
              <Typography variant="h6">Items:</Typography>
              {selectedOrder.items.map((item, index) => (
                <Typography key={index}>
                  {item.menuItem.name} x {item.quantity} - $
                  {(item.price * item.quantity).toFixed(2)}
                </Typography>
              ))}
              <Typography variant="h6" sx={{ marginTop: 2 }}>
                Total: ${selectedOrder.totalAmount.toFixed(2)}
              </Typography>
              {selectedOrder.specialInstructions && (
                <Typography sx={{ marginTop: 1 }}>
                  Special Instructions: {selectedOrder.specialInstructions}
                </Typography>
              )}
            </>
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)} color="primary">
            Close
          </Button>
        </DialogActions>
      </Dialog>
    </StyledContainer>
  );
}

export default Orders;

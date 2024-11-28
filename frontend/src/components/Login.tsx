import React, {useState} from 'react';
import {useAppDispatch, useAppSelector} from '../hooks/hooks';
import {loginUser} from '../app/slices/authSlice';
import {useNavigate} from 'react-router-dom';
import {Alert, Box, Button, CircularProgress, Container, TextField} from '@mui/material';

const Login: React.FC = () => {
    const [username, setUsername] = useState('');
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const {loading, error} = useAppSelector((state) => state.auth);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (username.trim()) {
            const resultAction = await dispatch(loginUser(username.trim()));
            if (loginUser.fulfilled.match(resultAction)) {
                if (resultAction.payload.role === 'admin') {
                    navigate('/');
                } else if (resultAction.payload.role === 'user') {
                    navigate('/user');
                }
            }
        }
    };

    return (<Container maxWidth="sm">
            <Box mt={10} p={4} boxShadow={3} borderRadius={2}>
                {error && <Alert severity="error">{error}</Alert>}
                <form onSubmit={handleSubmit}>
                    <TextField
                        label="Username (admin or userId)"
                        variant="outlined"
                        fullWidth
                        required
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        margin="normal"
                    />
                    <Button type="submit" variant="contained" color="primary" fullWidth disabled={loading}>
                        {loading ? <CircularProgress size={24}/> : 'Zaloguj się'}
                    </Button>
                </form>
            </Box>
        </Container>);
};

export default Login;

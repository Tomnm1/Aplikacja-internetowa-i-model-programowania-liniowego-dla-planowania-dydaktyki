import React, { useState } from 'react';
import { useAppDispatch } from '../hooks/hooks';
import { login } from '../app/slices/authSlice.ts';
import { useNavigate } from 'react-router-dom';
import { Container, TextField, Button, Box, Alert } from '@mui/material';

const Login: React.FC = () => {
    const [username, setUsername] = useState('');
    const [error, setError] = useState<string | null>(null);
    const dispatch = useAppDispatch();
    const navigate = useNavigate();

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (username.trim()) {
            dispatch(login(username.trim()));
            navigate('/');
        } else {
            setError('admin lub user');
        }
    };

    return (
        <Container maxWidth="sm">
            <Box mt={10} p={4} boxShadow={3} borderRadius={2}>
                {error && <Alert severity="error">{error}</Alert>}
                <form onSubmit={handleSubmit}>
                    <TextField
                        label="admin lub user"
                        variant="outlined"
                        fullWidth
                        required
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        margin="normal"
                    />
                    <Button type="submit" variant="contained" color="primary" fullWidth>
                        Wbijamy
                    </Button>
                </form>
            </Box>
        </Container>
    );
};

export default Login;

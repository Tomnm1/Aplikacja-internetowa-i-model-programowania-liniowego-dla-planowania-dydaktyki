import React from 'react';
import { Container, Typography, Box } from '@mui/material';

const UserTest: React.FC = () => {
    return (
        <Container>
            <Box mt={5}>
        <Typography variant="h4">Witaj Jacku!</Typography>
    <Typography variant="body1" mt={2}>
        To jest interface testowy.
    </Typography>
    </Box>
    </Container>
);
};

export default UserTest;

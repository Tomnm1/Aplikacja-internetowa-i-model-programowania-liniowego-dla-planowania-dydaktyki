import {Box, Container, Typography} from '@mui/material';

//todo zmienic typ propsa
const UserHome = (props: any) => {
    console.log(props);
    return (<Container>
            <Box mt={5}>
                <Typography variant="h4">Witaj Jacku!</Typography>
                <Typography variant="body1" mt={2}>
                    To jest interface testowy.
                </Typography>
            </Box>
        </Container>);
};

export default UserHome;

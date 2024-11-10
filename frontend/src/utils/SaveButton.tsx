import React from 'react';
import Box from '@mui/material/Box';
import CircularProgress from '@mui/material/CircularProgress';
import { green } from '@mui/material/colors';
import Fab from '@mui/material/Fab';
import CheckIcon from '@mui/icons-material/Check';
import SaveIcon from '@mui/icons-material/Save';

interface SaveButtonProps {
    onClick: () => void;
    loading: boolean;
    success: boolean;
}

const SaveButton: React.FC<SaveButtonProps> = ({ onClick, loading, success }) => {
    const buttonSx = {
        ...(success && {
            bgcolor: green[500],
            '&:hover': {
                bgcolor: green[700],
            },
        }),
    };

    return (
        <Box className="flex items-center justify-center">
            <Box className="relative m-1">
                <Fab
                    aria-label="save"
                    color="primary"
                    size="medium"
                    sx={buttonSx}
                    onClick={onClick}
                    disabled={loading || success}
                >
                    {success ? <CheckIcon /> : <SaveIcon />}
                </Fab>
                {loading && (
                    <CircularProgress
                        size={68}
                        sx={{
                            color: green[500],
                            position: 'absolute',
                            top: -3,
                            left: -3,
                            zIndex: 1,
                        }}
                    />
                )}
            </Box>
        </Box>
    );
};

export default SaveButton;

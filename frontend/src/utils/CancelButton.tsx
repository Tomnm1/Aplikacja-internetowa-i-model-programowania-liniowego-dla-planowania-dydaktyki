import React from 'react';
import Box from '@mui/material/Box';
import Fab from '@mui/material/Fab';
import ClearIcon from '@mui/icons-material/Clear';

interface CancelButtonProps {
    onClick: () => void;
    disabled: boolean;
}

const CancelButton: React.FC<CancelButtonProps> = ({ onClick, disabled }) => {
    return (
        <Box className="flex items-center justify-center">
            <Box className="relative m-1">
                <Fab
                    aria-label="cancel"
                    color="secondary"
                    onClick={onClick}
                    disabled={disabled}
                    size="medium"
                    sx={{
                        bgcolor: 'red',
                        '&:hover': {
                            bgcolor: 'darkred',
                        },
                    }}
                >
                    <ClearIcon />
                </Fab>
            </Box>
        </Box>
    );
};

export default CancelButton;

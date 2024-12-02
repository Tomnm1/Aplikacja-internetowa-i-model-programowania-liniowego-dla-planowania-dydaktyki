import React from 'react';
import Box from '@mui/material/Box';
import Fab from '@mui/material/Fab';
import ClearIcon from '@mui/icons-material/Clear';
import {Tooltip} from "@mui/material";

interface CancelButtonProps {
    onClick: () => void;
    disabled: boolean;
    tooltipText?: string;
}

const CancelButton: React.FC<CancelButtonProps> = ({ onClick, disabled, tooltipText = "Anuluj" }) => {
    return (
        <Box className="flex items-center justify-center">
            <Box className="relative m-1">
                <Tooltip title={tooltipText}>
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
                </Tooltip>
            </Box>
        </Box>
    );
};

export default CancelButton;

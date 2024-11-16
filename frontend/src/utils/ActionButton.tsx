import React from 'react';
import Box from '@mui/material/Box';
import Fab from '@mui/material/Fab';
import { Tooltip } from '@mui/material';
import AbcIcon from '@mui/icons-material/Abc';

interface CancelButtonProps {
    onClick: () => void;
    disabled: boolean;
    tooltipText?: string;
    icon: React.ReactElement;
    colorScheme?: 'primary' | 'secondary';
}

const ActionButton: React.FC<CancelButtonProps> = ({onClick, disabled, tooltipText = "Wykonaj", icon=<AbcIcon />, colorScheme = 'primary',}) => {
    const getFabStyles = () => {
        switch (colorScheme) {
            case 'primary':
                return {
                    bgcolor: 'primary',
                    '&:hover': {
                        bgcolor: 'steelblue',
                    },
                };
            case 'secondary':
                return {
                    bgcolor: 'red',
                    '&:hover': {
                        bgcolor: 'darkred',
                    },
                };
            default:
                return {};
        }
    };
    return (
        <Box display="flex" alignItems="center" justifyContent="center">
            <Box position="relative" m={1}>
                <Tooltip title={tooltipText}>
                    <Fab
                        aria-label={tooltipText}
                        color={colorScheme}
                        onClick={onClick}
                        disabled={disabled}
                        size="medium"
                        sx={getFabStyles}
                    >
                        {icon}
                    </Fab>
                </Tooltip>
            </Box>
        </Box>
    );
};

export default ActionButton;

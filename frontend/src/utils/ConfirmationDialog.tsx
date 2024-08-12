import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Paper, { PaperProps } from '@mui/material/Paper';
import Draggable from 'react-draggable';

function PaperComponent(props: PaperProps) {
    return (
        <Draggable
            handle="#dialog"
            cancel={'[class*="MuiDialogContent-root"]'}
        >
            <Paper {...props} />
        </Draggable>
    );
}

interface ConfirmationDialogProps {
    open: boolean;
    onClose: (confirmed: boolean) => void;
    title: string,
    content: string,
    action: string
}

export default function ConfirmationDialog({ open, onClose, title, content, action }: ConfirmationDialogProps) {
    const handleClose = (confirmed: boolean) => {
        onClose(confirmed);
    };

    return (
        <Dialog
            open={open}
            onClose={() => handleClose(false)}
            PaperComponent={PaperComponent}
            aria-labelledby="dialog"
        >
            <DialogTitle style={{ cursor: 'move' }} id="dialog">
                {title}
            </DialogTitle>
            <DialogContent>
                <DialogContentText>
                    {content}
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button onClick={() => handleClose(false)}>
                    Anuluj
                </Button>
                <Button onClick={() => handleClose(true)}>
                    {action}
                </Button>
            </DialogActions>
        </Dialog>
    );
}

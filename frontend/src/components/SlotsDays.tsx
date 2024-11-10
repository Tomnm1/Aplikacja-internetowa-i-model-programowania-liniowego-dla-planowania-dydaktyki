import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import {
    DataGrid,
    GridColDef,
    GridToolbarContainer,
    GridActionsCellItem,
    GridRowId,
    GridRowParams, GridToolbar,
} from '@mui/x-data-grid';
import {
    Button,
} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import VisibilityIcon from '@mui/icons-material/Visibility';
import ConfirmationDialog from '../utils/ConfirmationDialog';
import { RootState, AppDispatch } from '../app/store';
import {Day, SlotsDay} from '../utils/Interfaces.ts';
import { plPL } from '@mui/x-data-grid/locales';
import {deleteSlotsDay, fetchSlotsDays} from "../app/slices/slotsDaysSlice.ts";
import SlotsDayModal from "./SlotsDayModal.tsx";


const SlotsDays: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();
    const slotsDays = useSelector((state: RootState) => state.slotsDays.rows);
    const loading = useSelector((state: RootState) => state.slotsDays.loading);
    const error = useSelector((state: RootState) => state.slotsDays.error);

    const [isDialogOpen, setDialogOpen] = React.useState(false);
    const [selectedRowId, setSelectedRowId] = React.useState<GridRowId | null>(null);
    const [isModalOpen, setModalOpen] = React.useState(false);
    const [selectedSlotsDay, setSelectedSlotsDay] = React.useState<SlotsDay | null>(null);
    const [isAdding, setIsAdding] = React.useState(false);

    useEffect(() => {
        dispatch(fetchSlotsDays());
    }, [dispatch]);

    const dayMapping: { [key in Day]: string } = {
        [Day.MONDAY]: 'Poniedziałek',
        [Day.TUESDAY]: 'Wtorek',
        [Day.WEDNESDAY]: 'Środa',
        [Day.THURSDAY]: 'Czwartek',
        [Day.FRIDAY]: 'Piątek',
        [Day.SATURDAY]: 'Sobota',
        [Day.SUNDAY]: 'Niedziela',
    };

    const handleViewClick = (id: GridRowId) => () => {
        const slotsDay = slotsDays.find((sd) => sd.id === id);
        if (slotsDay) {
            setSelectedSlotsDay(slotsDay);
            setIsAdding(false);
            setModalOpen(true);
        }
    };

    const handleDeleteClick = (id: GridRowId) => () => {
        setSelectedRowId(id);
        setDialogOpen(true);
    };

    const handleDialogClose = (confirmed: boolean) => {
        if (confirmed && selectedRowId != null) {
            if (typeof selectedRowId === "number") {
                dispatch(deleteSlotsDay(selectedRowId));
            }
        }
        setDialogOpen(false);
        setSelectedRowId(null);
    };

    const handleAddClick = () => {
        setSelectedSlotsDay(null);
        setIsAdding(true);
        setModalOpen(true);
    };

    const columns: GridColDef[] = [
        {
            field: 'day',
            headerName: 'Dzień',
            width: 150,
            valueGetter: (value) => dayMapping[value] || value,
        },
        { field: 'slotRepresentation', headerName: 'Slot', width: 300 },
        {
            field: 'actions',
            type: 'actions',
            headerName: 'Akcje',
            getActions: (params: GridRowParams) => [
                <GridActionsCellItem
                    icon={<VisibilityIcon />}
                    label="Szczegóły"
                    onClick={handleViewClick(params.id)}
                    color="inherit"
                />,
                <GridActionsCellItem
                    icon={<DeleteIcon />}
                    label="Usuń"
                    onClick={handleDeleteClick(params.id)}
                    color="inherit"
                />,
            ],
        },
    ];
    const TopToolbar = () => (
        <GridToolbarContainer>
            <Button color="primary" startIcon={<AddIcon />} onClick={handleAddClick}>
                Dodaj slot dnia
            </Button>
            <GridToolbar/>
        </GridToolbarContainer>
    );

    return (
        <>
            <DataGrid
                rows={slotsDays}
                columns={columns}
                loading={loading}
                localeText={plPL.components.MuiDataGrid.defaultProps.localeText}
                slots={{ toolbar: TopToolbar }}
            />
            <ConfirmationDialog
                open={isDialogOpen}
                onClose={handleDialogClose}
                title="Potwierdzenie"
                content="Czy na pewno chcesz usunąć ten slot dnia?"
                action="Potwierdź"
            />
            {isModalOpen && (
                <SlotsDayModal
                    open={isModalOpen}
                    onClose={() => setModalOpen(false)}
                    slotsDay={selectedSlotsDay}
                    isAdding={isAdding}
                />
            )}
            {error && <div style={{ color: 'red' }}>Błąd: {error}</div>}
        </>
    );
};

export default SlotsDays;

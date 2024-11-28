import React, {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {
    DataGrid, GridActionsCellItem, GridColDef, GridRowParams, GridToolbar, GridToolbarContainer,
} from '@mui/x-data-grid';
import {Button} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import VisibilityIcon from '@mui/icons-material/Visibility';
import ConfirmationDialog from '../utils/ConfirmationDialog';
import {AppDispatch, RootState} from '../app/store';
import {Day, dayMapping, SlotsDay} from '../utils/Interfaces';
import {plPL} from '@mui/x-data-grid/locales';
import {deleteSlotsDay, fetchSlotsDays} from "../app/slices/slotsDaysSlice";
import SlotsDayModal from "./SlotsDayModal";
import {useSnackbar} from "notistack";

const SlotsDays: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();
    const {rows: slotsDays, loading} = useSelector((state: RootState) => state.slotsDays);

    const [isDialogOpen, setDialogOpen] = React.useState(false);
    const [selectedRowId, setSelectedRowId] = React.useState<number | null>(null);
    const [isModalOpen, setModalOpen] = React.useState(false);
    const [selectedSlotsDay, setSelectedSlotsDay] = React.useState<SlotsDay | null>(null);
    const [isAdding, setIsAdding] = React.useState(false);
    const {enqueueSnackbar} = useSnackbar();

    useEffect(() => {
        dispatch(fetchSlotsDays()).unwrap().catch((error) => {
            enqueueSnackbar(`Błąd podczas pobierania slotów dnia: ${error.message}`, {variant: 'error'});
        });
    }, [dispatch, enqueueSnackbar]);

    const handleViewClick = (id: number) => () => {
        const slotsDay = slotsDays.find((sd) => sd.id === id);
        if (slotsDay) {
            setSelectedSlotsDay(slotsDay);
            setIsAdding(false);
            setModalOpen(true);
        }
    };

    const handleDeleteClick = (id: number) => () => {
        setSelectedRowId(id);
        setDialogOpen(true);
    };

    const handleDialogClose = (confirmed: boolean) => {
        if (confirmed && selectedRowId != null) {
            dispatch(deleteSlotsDay(selectedRowId))
                .unwrap()
                .then(() => {
                    enqueueSnackbar('Slot dnia został pomyślnie usunięty', {variant: 'success'});
                })
                .catch((error) => {
                    enqueueSnackbar(`Błąd podczas usuwania slotu dnia: ${error.message}`, {variant: 'error'});
                });
        }
        setDialogOpen(false);
        setSelectedRowId(null);
    };

    const handleAddClick = () => {
        setSelectedSlotsDay(null);
        setIsAdding(true);
        setModalOpen(true);
    };

    const columns: GridColDef[] = [{
        field: 'day', headerName: 'Dzień', width: 150, valueGetter: (params) => dayMapping[params as Day],
    }, {field: 'slotRepresentation', headerName: 'Slot', width: 300}, {
        field: 'actions',
        type: 'actions',
        headerName: 'Akcje',
        getActions: (params: GridRowParams) => [<GridActionsCellItem
            icon={<VisibilityIcon/>}
            label="Szczegóły"
            onClick={handleViewClick(params.id as number)}
            color="inherit"
        />, <GridActionsCellItem
            icon={<DeleteIcon/>}
            label="Usuń"
            onClick={handleDeleteClick(params.id as number)}
            color="inherit"
        />,],
    },];

    const TopToolbar = () => (<GridToolbarContainer>
            <Button color="primary" startIcon={<AddIcon/>} onClick={handleAddClick}>
                Dodaj slot dnia
            </Button>
            <div style={{flexGrow: 1}}/>
            <GridToolbar/>
        </GridToolbarContainer>);

    return (<>
            <DataGrid
                rows={slotsDays}
                columns={columns}
                loading={loading}
                localeText={plPL.components.MuiDataGrid.defaultProps.localeText}
                slots={{toolbar: TopToolbar}}
            />
            <ConfirmationDialog
                open={isDialogOpen}
                onClose={handleDialogClose}
                title="Potwierdzenie"
                content="Czy na pewno chcesz usunąć ten slot dnia?"
                action="Potwierdź"
            />
            {isModalOpen && (<SlotsDayModal
                    open={isModalOpen}
                    onClose={() => setModalOpen(false)}
                    slotsDay={selectedSlotsDay}
                    isAdding={isAdding}
                />)}
        </>);
};

export default SlotsDays;

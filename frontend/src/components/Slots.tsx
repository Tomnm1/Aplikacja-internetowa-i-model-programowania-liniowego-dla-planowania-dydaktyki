//todo zamienić pola tekstowe na timepickery ( <TimePicker> @mui/x-date-pickers/ + @mui/x-date-pickers/AdapterDateFnsV3
import {useDispatch, useSelector} from 'react-redux';
import {
    DataGrid,
    GridActionsCellItem,
    GridColDef,
    GridRowId,
    GridRowModel,
    GridRowModes,
    GridRowModesModel,
    GridRowParams,
    GridToolbar,
    GridToolbarContainer,
} from '@mui/x-data-grid';
import {Button} from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Close';
import ConfirmationDialog from '../utils/ConfirmationDialog';
import {AppDispatch, RootState} from '../app/store';
import {Slot} from '../utils/Interfaces.ts';
import {plPL} from "@mui/x-data-grid/locales";
import {useEffect, useState} from "react";
import {
    addNewSlot,
    addSlot,
    clearSelectedRow,
    deleteSlot,
    fetchSlots,
    removeNewSlot,
    setRowModesModel,
    setSelectedRow, updateSlot
} from "../app/slices/slotsSlice.ts";

const Slots: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();
    const rows = useSelector((state: RootState) => state.slots.rows);
    const rowModesModel = useSelector((state: RootState) => state.slots.rowModesModel);
    const selectedRowId = useSelector((state: RootState) => state.slots.selectedRowId);
    const loading = useSelector((state: RootState) => state.slots.loading);
    const error = useSelector((state: RootState) => state.slots.error);
    const [isDialogOpen, setDialogOpen] = useState(false);

    const [rowIdCounter, setRowIdCounter] = useState(-1);

    useEffect(() => {
        dispatch(fetchSlots());
    }, [dispatch]);


    const handleEditClick = (id: GridRowId) => () => {
        dispatch(setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.Edit } }));
    };

    const handleSaveClick = (id: GridRowId) => () => {
        dispatch(setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.View } }));
    };

    const handleDeleteClick = (id: GridRowId) => () => {
        const rowToDelete = rows.find((row) => row.slot_id === id);
        if (rowToDelete) {
            dispatch(setSelectedRow({id, start: rowToDelete.start_time,  stop: rowToDelete.end_time}));
            setDialogOpen(true);
        }
    };

    const handleDialogClose = (confirmed: boolean) => {
        if (confirmed && selectedRowId != null) {
            dispatch(deleteSlot(selectedRowId));
        }
        setDialogOpen(false);
        dispatch(clearSelectedRow());
    };

    const handleCancelClick = (id: GridRowId) => () => {
        dispatch(
            setRowModesModel({
                ...rowModesModel,
                [id]: { mode: GridRowModes.View, ignoreModifications: true },
            })
        );

        const editedRow = rows.find((row) => row.slot_id === id);
        if (editedRow?.isNew) {
            dispatch(removeNewSlot(id));
        }
    };

    const processRowUpdate = async (newRow: GridRowModel) => {
        const updatedRow: Slot = {
            slot_id: newRow.slot_id,
            start_time: newRow.start_time,
            end_time: newRow.end_time,
            isNew: newRow.isNew || false,
        };

        if (updatedRow.isNew) {
            const resultAction = await dispatch(addSlot(updatedRow));
            if (addSlot.fulfilled.match(resultAction)) {
                const { slot } = resultAction.payload;
                return slot;
            } else {
                throw new Error('Failed to add slot');
            }
        } else {
            const resultAction = await dispatch(updateSlot(updatedRow));
            if (updateSlot.fulfilled.match(resultAction)) {
                return resultAction.payload;
            } else {
                throw new Error('Failed to update slot');
            }
        }
    };

    const handleRowModesModelChange = (newRowModesModel: GridRowModesModel) => {
        dispatch(setRowModesModel(newRowModesModel));
    };

    const handleAddClick = () => {
        const id = rowIdCounter;
        setRowIdCounter((prev) => prev - 1);
        const newSlot: Slot = {
            slot_id: id,
            start_time: "00:00",
            end_time: "00:00",
            isNew: true,
        };
        dispatch(addNewSlot(newSlot));
        dispatch(
            setRowModesModel({
                ...rowModesModel,
                [id]: { mode: GridRowModes.Edit, fieldToFocus: 'start_time' },
            })
        );
    };

    const TopToolbar = () => {
        return (
            <GridToolbarContainer>
                <Button color="primary" startIcon={<AddIcon />} onClick={handleAddClick}>
                    Dodaj slot
                </Button>
                <GridToolbar />
            </GridToolbarContainer>
        );
    };

    const columns: GridColDef[] = [
        { field: 'start_time', headerName: 'Od', width: 150, editable: true },
        { field: 'end_time', headerName: 'Do', width: 150, editable: true },
        {
            field: 'actions',
            type: 'actions',
            headerName: 'Akcje',
            width: 100,
            getActions: (params: GridRowParams) => {
                const id = params.id;
                const isInEditMode = rowModesModel[id]?.mode === GridRowModes.Edit;

                if (isInEditMode) {
                    return [
                        <GridActionsCellItem
                            key="save"
                            icon={<SaveIcon />}
                            label="Zapisz"
                            sx={{ color: 'primary.main' }}
                            onClick={handleSaveClick(id)}
                        />,
                        <GridActionsCellItem
                            key="cancel"
                            icon={<CancelIcon />}
                            label="Anuluj"
                            onClick={handleCancelClick(id)}
                            color="inherit"
                        />,
                    ];
                }

                return [
                    <GridActionsCellItem
                        key="edit"
                        icon={<EditIcon />}
                        label="Edytuj"
                        onClick={handleEditClick(id)}
                        color="inherit"
                    />,
                    <GridActionsCellItem
                        key="delete"
                        icon={<DeleteIcon />}
                        label="Usuń"
                        onClick={handleDeleteClick(id)}
                        color="inherit"
                    />,
                ];
            },
        },
    ];

    return (
        <>
            <DataGrid
                rows={rows}
                columns={columns}
                loading={loading}
                localeText={plPL.components.MuiDataGrid.defaultProps.localeText}
                editMode="row"
                rowModesModel={rowModesModel}
                onRowModesModelChange={handleRowModesModelChange}
                processRowUpdate={processRowUpdate}
                slots={{ toolbar: TopToolbar }}
                getRowId={(row) => row.slot_id}
            />
            <ConfirmationDialog
                open={isDialogOpen}
                onClose={handleDialogClose}
                title="Potwierdzenie"
                content={`Czy na pewno chcesz usunąć budynek slot?`}
                action="Potwierdź"
            />
            {error && <div style={{ color: 'red' }}>Błąd: {error}</div>}
        </>
    );
};

export default Slots;

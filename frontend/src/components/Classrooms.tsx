// Classrooms.tsx
import * as React from 'react';
import Button from '@mui/material/Button';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Close';

import {
    GridRowModes,
    DataGrid,
    GridToolbarContainer,
    GridActionsCellItem,
    GridRowEditStopReasons,
    GridRowId,
    GridRowModel,
    GridEventListener,
    GridRowModesModel, GridToolbar,
} from '@mui/x-data-grid';
import { useDispatch, useSelector } from 'react-redux';
import { RootState, AppDispatch } from '../app/store';
import {
    addClassroom,
    updateClassroom,
    deleteClassroom,
    setRowModesModel,
} from '../app/slices/roomSlice.ts';
import ConfirmationDialog from '../utils/ConfirmationDialog';
import {useState} from "react";

const floors = ['First Floor', 'Second Floor', 'Third Floor'];

const TopToolbar = () => {
    const dispatch = useDispatch<AppDispatch>();

    const handleClick = () => {
        const id = String(Date.now());
        const newClassroom = { id, name: '', capacity: '', floor: '', isNew: true };
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-expect-error
        dispatch(addClassroom(newClassroom));
        dispatch(setRowModesModel({
            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
            [id]: { mode: GridRowModes.Edit, fieldToFocus: 'name' },
        }));
    };

    return (
        <GridToolbarContainer>
            <Button color="primary" startIcon={<AddIcon />} onClick={handleClick}>
                Dodaj salę
            </Button>
            <GridToolbar />
        </GridToolbarContainer>
    );
};

const Classrooms: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();
    const rows = useSelector((state: RootState) => state.classroom.rows);
    const rowModesModel = useSelector((state: RootState) => state.classroom.rowModesModel);

    const [isDialogOpen, setDialogOpen] = useState(false);
    const [selectedRowId, setSelectedRowId] = useState<GridRowId | null>(null);
    const [selectedRowName, setSelectedRowName] = useState<string | null>(null);

    const handleRowEditStop: GridEventListener<'rowEditStop'> = (params, event) => {
        if (params.reason === GridRowEditStopReasons.rowFocusOut) {
            event.defaultMuiPrevented = true;
        }
    };

    const handleEditClick = (id: GridRowId) => () => {
        dispatch(setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.Edit } }));
    };

    const handleSaveClick = (id: GridRowId) => () => {
        dispatch(setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.View } }));
    };

    const handleDeleteClick = (id: GridRowId) => () => {
        const rowToDelete = rows.find((row) => row.id === id);
        setSelectedRowId(id);
        setSelectedRowName(rowToDelete?.name || '');
        setDialogOpen(true);
    };

    const handleDialogClose = (confirmed: boolean) => {
        if (confirmed && selectedRowId) {
            dispatch(deleteClassroom(selectedRowId));
        }
        setDialogOpen(false);
        setSelectedRowId(null);
        setSelectedRowName(null);
    };

    const handleCancelClick = (id: GridRowId) => () => {
        dispatch(setRowModesModel({
            ...rowModesModel,
            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
            [id]: { mode: GridRowModes.View, ignoreModifications: true },
        }));

        const editedRow = rows.find((row: { id: GridRowId; }) => row.id === id);
        if (editedRow?.isNew) {
            dispatch(deleteClassroom(id));
        }
    };

    const processRowUpdate = (newRow: GridRowModel) => {
        const updatedRow = {
            ...newRow,
            isNew: false,
            id: newRow.id as GridRowId,
            name: newRow.name as string,
            capacity: newRow.capacity as number,
            floor: newRow.floor as string,
        };
        dispatch(updateClassroom(updatedRow));
        return updatedRow;
    };

    const handleRowModesModelChange = (newRowModesModel: GridRowModesModel) => {
        dispatch(setRowModesModel(newRowModesModel));
    };

    const columns = [
        { field: 'name', headerName: 'Nazwa', width: 180, editable: true },
        {
            field: 'capacity',
            headerName: 'Pojemność',
            type: 'number',
            width: 120,
            align: 'left',
            headerAlign: 'left',
            editable: true,
        },
        {
            field: 'floor',
            headerName: 'Piętro',
            width: 180,
            editable: true,
            type: 'singleSelect',
            valueOptions: floors,
        },
        {
            field: 'actions',
            type: 'actions',
            headerName: 'Actions',
            width: 100,
            cellClassName: 'actions',
            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-expect-error
            getActions: ({ id }) => {
                const isInEditMode = rowModesModel[id]?.mode === GridRowModes.Edit;

                if (isInEditMode) {
                    return [
                        <GridActionsCellItem
                            icon={<SaveIcon />}
                            label="Save"
                            sx={{ color: 'primary.main' }}
                            onClick={handleSaveClick(id)}
                        />,
                        <GridActionsCellItem
                            icon={<CancelIcon />}
                            label="Cancel"
                            className="textPrimary"
                            onClick={handleCancelClick(id)}
                            color="inherit"
                        />,
                    ];
                }

                return [
                    <GridActionsCellItem
                        icon={<EditIcon />}
                        label="Edit"
                        className="textPrimary"
                        onClick={handleEditClick(id)}
                        color="inherit"
                    />,
                    <GridActionsCellItem
                        icon={<DeleteIcon />}
                        label="Delete"
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
                // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                // @ts-expect-error
                columns={columns}
                editMode="row"
                // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                // @ts-expect-error
                rowModesModel={rowModesModel}
                onRowModesModelChange={handleRowModesModelChange}
                onRowEditStop={handleRowEditStop}
                processRowUpdate={processRowUpdate}
                slots={{ toolbar: TopToolbar }}
            />
            <ConfirmationDialog
                open={isDialogOpen}
                onClose={handleDialogClose}
                title={"Potwierdzenie"}
                content={`Czy na pewno chcesz usunąć salę ${selectedRowName}`}
                action={"Potwierdź"}
            />
        </>
    );
};

export default Classrooms;

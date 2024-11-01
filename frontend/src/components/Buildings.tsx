// Buildings.tsx

import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import {
    GridRowModes,
    DataGrid,
    GridToolbarContainer,
    GridActionsCellItem,
    GridRowId,
    GridRowModesModel,
    GridToolbar,
    GridColDef,
    GridRowModel,
    GridRowParams,
} from '@mui/x-data-grid';
import { Button } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Close';
import ConfirmationDialog from '../utils/ConfirmationDialog';
import { RootState, AppDispatch } from '../app/store';
import {
    setRowModesModel,
    setSelectedRow,
    clearSelectedRow,
    fetchBuildings,
    addBuilding,
    updateBuilding,
    deleteBuilding,
    addNewBuilding,
    removeNewBuilding,
} from '../app/slices/buildingSlice';
import { Building } from '../utils/Interfaces.ts';

const Buildings: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();
    const rows = useSelector((state: RootState) => state.buildings.rows);
    const rowModesModel = useSelector((state: RootState) => state.buildings.rowModesModel);
    const selectedRowCode = useSelector((state: RootState) => state.buildings.selectedRowCode);
    const selectedRowId = useSelector((state: RootState) => state.buildings.selectedRowId);
    const loading = useSelector((state: RootState) => state.buildings.loading);
    const error = useSelector((state: RootState) => state.buildings.error);
    const [isDialogOpen, setDialogOpen] = React.useState(false);

    const [rowIdCounter, setRowIdCounter] = React.useState(-1);

    useEffect(() => {
        dispatch(fetchBuildings());
    }, [dispatch]);

    const handleEditClick = (id: GridRowId) => () => {
        dispatch(setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.Edit } }));
    };

    const handleSaveClick = (id: GridRowId) => () => {
        dispatch(setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.View } }));
    };

    const handleDeleteClick = (id: GridRowId) => () => {
        const rowToDelete = rows.find((row) => row.id === id);
        if (rowToDelete) {
            dispatch(setSelectedRow({ id, code: rowToDelete.code }));
            setDialogOpen(true);
        }
    };

    const handleDialogClose = (confirmed: boolean) => {
        if (confirmed && selectedRowId != null) {
            dispatch(deleteBuilding(selectedRowId));
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

        const editedRow = rows.find((row) => row.id === id);
        if (editedRow?.isNew) {
            dispatch(removeNewBuilding(id));
        }
    };

    const processRowUpdate = (newRow: GridRowModel) => {
        const updatedRow: Building = {
            id: newRow.id,
            code: newRow.code || '',
            isNew: newRow.isNew || false,
        };

        if (updatedRow.isNew) {
            dispatch(addBuilding(updatedRow));
        } else {
            dispatch(updateBuilding(updatedRow));
        }
        return updatedRow;
    };

    const handleRowModesModelChange = (newRowModesModel: GridRowModesModel) => {
        dispatch(setRowModesModel(newRowModesModel));
    };

    const columns: GridColDef[] = [
        { field: 'code', headerName: 'Kod', width: 150, editable: true },
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

    const handleAddClick = () => {
        const id = rowIdCounter;
        setRowIdCounter((prev) => prev - 1);
        const newBuilding: Building = {
            id,
            code: '',
            isNew: true,
        };
        dispatch(addNewBuilding(newBuilding));
        dispatch(
            setRowModesModel({
                ...rowModesModel,
                [id]: { mode: GridRowModes.Edit, fieldToFocus: 'code' },
            })
        );
    };

    const TopToolbar = () => {
        return (
            <GridToolbarContainer>
                <Button color="primary" startIcon={<AddIcon />} onClick={handleAddClick}>
                    Dodaj budynek
                </Button>
                <GridToolbar />
            </GridToolbarContainer>
        );
    };

    return (
        <>
            <DataGrid
                rows={rows}
                columns={columns}
                loading={loading}
                editMode="row"
                rowModesModel={rowModesModel}
                onRowModesModelChange={handleRowModesModelChange}
                processRowUpdate={processRowUpdate}
                slots={{ toolbar: TopToolbar }}
            />
            <ConfirmationDialog
                open={isDialogOpen}
                onClose={handleDialogClose}
                title="Potwierdzenie"
                content={`Czy na pewno chcesz usunąć budynek ${selectedRowCode}?`}
                action="Potwierdź"
            />
            {error && <div style={{ color: 'red' }}>Błąd: {error}</div>}
        </>
    );
};

export default Buildings;

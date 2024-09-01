import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import {
    GridRowModes,
    DataGrid,
    GridToolbarContainer,
    GridActionsCellItem,
    GridRowEditStopReasons,
    GridRowId,
    GridRowModel,
    GridEventListener,
    GridRowModesModel,
    GridToolbar,
} from '@mui/x-data-grid';
import Button from '@mui/material/Button';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/DeleteOutlined';
import SaveIcon from '@mui/icons-material/Save';
import CancelIcon from '@mui/icons-material/Close';
import ConfirmationDialog from "../utils/ConfirmationDialog.tsx";
import { RootState } from '../app/store';
import {
    setRows,
    setRowModesModel,
    addEmployee,
    updateEmployee,
    deleteEmployee,
    setSelectedRow,
    clearSelectedRow,
} from '../app/slices/employeesSlice';

const roles = ['Market', 'Finance', 'Development'];

function TopToolbar() {
    const dispatch = useDispatch();

    const handleClick = () => {
        const id = String(Math.random());
        const newEmployee = { id, name: '', age: '', joinDate: new Date(), role: '', isNew: true };
        dispatch(addEmployee(newEmployee));
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-expect-error
        dispatch(setRowModesModel((oldModel: GridRowModesModel) => ({
            ...oldModel,
            [id]: { mode: GridRowModes.Edit, fieldToFocus: 'name' },
        })));
    };

    return (
        <GridToolbarContainer>
            <Button color="primary" startIcon={<AddIcon />} onClick={handleClick}>
                Dodaj pracownika
            </Button>
            <GridToolbar />
        </GridToolbarContainer>
    );
}

const Pracownicy: React.FC = () => {
    const dispatch = useDispatch();
    const rows = useSelector((state: RootState) => state.employees.rows);
    const rowModesModel = useSelector((state: RootState) => state.employees.rowModesModel);
    const selectedRowName = useSelector((state: RootState) => state.employees.selectedRowName);
    const [isDialogOpen, setDialogOpen] = React.useState(false);

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
        if (rowToDelete) {
            dispatch(setSelectedRow({ id, name: rowToDelete.name }));
            setDialogOpen(true);
        }
    };

    const handleDialogClose = (confirmed: boolean) => {
        if (confirmed) {
            dispatch(deleteEmployee());
        }
        setDialogOpen(false);
        dispatch(clearSelectedRow());
    };

    const handleCancelClick = (id: GridRowId) => () => {
        dispatch(setRowModesModel({
            ...rowModesModel,
            [id]: { mode: GridRowModes.View, ignoreModifications: true },
        }));

        const editedRow = rows.find((row) => row.id === id);
        if (editedRow?.isNew) {
            dispatch(setRows(rows.filter((row) => row.id !== id)));
        }
    };

    const processRowUpdate = (newRow: GridRowModel) => {
        const updatedRow = {
            ...newRow,
            isNew: false,
        };
        dispatch(updateEmployee(updatedRow as never));
        return updatedRow;
    };

    const handleRowModesModelChange = (newRowModesModel: GridRowModesModel) => {
        dispatch(setRowModesModel(newRowModesModel));
    };

    const columns = [
        { field: 'name', headerName: 'Name', width: 180, editable: true },
        {
            field: 'age',
            headerName: 'Age',
            type: 'number',
            width: 80,
            align: 'left',
            headerAlign: 'left',
            editable: true,
        },
        {
            field: 'joinDate',
            headerName: 'Join date',
            type: 'date',
            width: 180,
            editable: true,
        },
        {
            field: 'role',
            headerName: 'Department',
            width: 220,
            editable: true,
            type: 'singleSelect',
            valueOptions: roles,
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
    // todo zmiana dencity w zaleznosci od rozdzielczosci

    return (
        <>
            <DataGrid
                rows={rows}
                /* eslint-disable-next-line @typescript-eslint/ban-ts-comment */
                // @ts-expect-error
                columns={columns}
                editMode="row"
                rowModesModel={rowModesModel}
                onRowModesModelChange={handleRowModesModelChange}
                onRowEditStop={handleRowEditStop}s
                processRowUpdate={processRowUpdate}
                slots={{ toolbar: TopToolbar }}
            />
            <ConfirmationDialog
                open={isDialogOpen}
                onClose={handleDialogClose}
                title={"Potwierdzenie"}
                content={`Czy na pewno chcesz usunąć rekord ${selectedRowName}`}
                action={"Potwierdź"}
            />
        </>
    );
};

export default Pracownicy;


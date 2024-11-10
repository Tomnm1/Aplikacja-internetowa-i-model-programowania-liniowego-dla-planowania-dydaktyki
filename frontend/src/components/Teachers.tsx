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
import {
    Button,
    Select,
    MenuItem,
    TextField,
    Checkbox,
    ListItemText,
    InputLabel,
    FormControl,
    OutlinedInput,
} from '@mui/material';
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
    fetchTeachers,
    addTeacher,
    updateTeacher,
    deleteTeacher,
    addNewTeacher,
    removeNewTeacher,
} from '../app/slices/teacherSlice.ts';
import {degrees, SubjectType, Teacher} from '../utils/Interfaces.ts';
import { API_ENDPOINTS } from '../app/urls.ts';
import {plPL} from "@mui/x-data-grid/locales";


const Teachers: React.FC = () => {
    const dispatch = useDispatch<AppDispatch>();
    const rows = useSelector((state: RootState) => state.teachers.rows);
    const rowModesModel = useSelector((state: RootState) => state.teachers.rowModesModel);
    const selectedRowName = useSelector((state: RootState) => state.teachers.selectedRowName);
    const selectedRowId = useSelector((state: RootState) => state.teachers.selectedRowId);
    const loading = useSelector((state: RootState) => state.teachers.loading);
    const error = useSelector((state: RootState) => state.teachers.error);
    const [isDialogOpen, setDialogOpen] = React.useState(false);



    const [subjectTypes, setSubjectTypes] = React.useState<SubjectType[]>([]);

    useEffect(() => {
        dispatch(fetchTeachers());
        fetch(API_ENDPOINTS.SUBJECT_TYPES)
            .then((res) => res.json())
            .then((data) => setSubjectTypes(data))
            .catch((err) => console.error('Failed to fetch subject types', err));
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
            dispatch(setSelectedRow({ id, name: `${rowToDelete.firstName} ${rowToDelete.lastName}` }));
            setDialogOpen(true);
        }
    };

    const handleDialogClose = (confirmed: boolean) => {
        if (confirmed && selectedRowId != null) {
            dispatch(deleteTeacher(selectedRowId));
        }
        setDialogOpen(false);
        dispatch(clearSelectedRow());
    };

    const handleCancelClick = (id: GridRowId) => () => {
        dispatch(
            setRowModesModel({
                ...rowModesModel,
                [id]: { mode: GridRowModes.View, ignoreModifications: true },
            }),
        );

        const editedRow = rows.find((row) => row.id === id);
        if (editedRow?.isNew) {
            dispatch(removeNewTeacher(id));
        }
    };

    const processRowUpdate = (newRow: GridRowModel) => {
        const updatedRow: Teacher = {
            id: newRow.id,
            firstName: newRow.firstName || '',
            lastName: newRow.lastName || '',
            degree: newRow.degree || '',
            preferences: newRow.preferences || {},
            subjectTypesList: newRow.subjectTypesList || [],
            isNew: newRow.isNew || false,
        };

        if (updatedRow.isNew) {
            dispatch(addTeacher(updatedRow));
        } else {
            dispatch(updateTeacher(updatedRow));
        }
        return updatedRow;
    };

    const handleRowModesModelChange = (newRowModesModel: GridRowModesModel) => {
        dispatch(setRowModesModel(newRowModesModel));
    };

    const columns: GridColDef[] = [
        { field: 'firstName', headerName: 'Imię', width: 150, editable: true },
        { field: 'lastName', headerName: 'Nazwisko', width: 150, editable: true },
        {
            field: 'degree',
            headerName: 'Stopień',
            width: 150,
            editable: true,
            renderEditCell: (params) => (
                <Select
                    value={params.value || ''}
                    onChange={(event) => {
                        params.api.setEditCellValue(
                            { id: params.id, field: 'degree', value: event.target.value },
                            event
                        );
                    }}
                    fullWidth
                    variant="outlined"
                >
                    {Object.entries(degrees).map(([key, displayName]) => (
                        <MenuItem key={key} value={key}>
                            {displayName}
                        </MenuItem>
                    ))}
                </Select>
            ),
            renderCell: (params) => degrees[params.value as keyof typeof degrees] || '',

        },
        {
            field: 'preferences',
            headerName: 'Preferencje',
            width: 200,
            editable: true,
            renderEditCell: (params) => (
                <TextField
                    value={JSON.stringify(params.value || {})}
                    onChange={(event) => {
                        try {
                            const value = JSON.parse(event.target.value);
                            params.api.setEditCellValue({ id: params.id, field: 'preferences', value }, event);
                        } catch {
                        }
                    }}
                    fullWidth
                />
            ),
            renderCell: (params) => JSON.stringify(params.value || {}),
        },
        {
            field: 'subjectTypesList',
            headerName: 'Typy Przedmiotów',
            width: 250,
            editable: true,
            renderEditCell: (params) => (
                <FormControl fullWidth>
                    <InputLabel id={`subject-types-label-${params.id}`}>Wybierz typy</InputLabel>
                    <Select
                        labelId={`subject-types-label-${params.id}`}
                        multiple
                        value={params.value || []}
                        onChange={(event) => {
                            const value = event.target.value;
                            params.api.setEditCellValue({ id: params.id, field: 'subjectTypesList', value }, event);
                        }}
                        input={<OutlinedInput label="Wybierz typy" />}
                        renderValue={(selected) =>
                            subjectTypes
                                .filter((type) => (selected as number[]).includes(type.id))
                                .map((type) => type.name)
                                .join(', ')
                        }
                        variant="outlined"
                    >
                        {subjectTypes.map((type) => (
                            <MenuItem key={type.id} value={type.id}>
                                <Checkbox checked={(params.value || []).includes(type.id)} />
                                <ListItemText primary={type.name} />
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
            ),
            renderCell: (params) =>
                params.value
                    ? subjectTypes
                        .filter((type) => (params.value as number[]).includes(type.id))
                        .map((type) => type.name)
                        .join(', ')
                    : '',
        },
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
                            icon={<SaveIcon />}
                            label="Zapisz"
                            sx={{ color: 'primary.main' }}
                            onClick={handleSaveClick(id)}
                        />,
                        <GridActionsCellItem
                            icon={<CancelIcon />}
                            label="Anuluj"
                            onClick={handleCancelClick(id)}
                            color="inherit"
                        />,
                    ];
                }

                return [
                    <GridActionsCellItem
                        icon={<EditIcon />}
                        label="Edytuj"
                        onClick={handleEditClick(id)}
                        color="inherit"
                    />,
                    <GridActionsCellItem
                        icon={<DeleteIcon />}
                        label="Usuń"
                        onClick={handleDeleteClick(id)}
                        color="inherit"
                    />,
                ];
            },
        },
    ];

    const [rowIdCounter, setRowIdCounter] = React.useState(-1);

    const handleAddClick = () => {
        const id = rowIdCounter;
        setRowIdCounter((prev) => prev - 1);
        const newTeacher: Teacher = {
            id,
            firstName: '',
            lastName: '',
            degree: "BRAK",
            preferences: {},
            subjectTypesList: [],
            isNew: true,
        };
        dispatch(addNewTeacher(newTeacher));
        dispatch(setRowModesModel({ ...rowModesModel, [id]: { mode: GridRowModes.Edit, fieldToFocus: 'firstName' } }));
    };

    const TopToolbar = () => {
        return (
            <GridToolbarContainer>
                <Button color="primary" startIcon={<AddIcon />} onClick={handleAddClick}>
                    Dodaj nauczyciela
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
                localeText={plPL.components.MuiDataGrid.defaultProps.localeText}
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
                content={`Czy na pewno chcesz usunąć nauczyciela ${selectedRowName}?`}
                action="Potwierdź"
            />
            {error && <div style={{ color: 'red' }}>Błąd: {error}</div>}
        </>
    );
};

export default Teachers;

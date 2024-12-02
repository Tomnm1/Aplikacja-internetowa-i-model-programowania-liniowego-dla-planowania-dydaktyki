import React, {useState} from 'react';
import {Box, IconButton, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow} from '@mui/material';
import {Delete, Edit, PersonAddAlt} from '@mui/icons-material';
import ErrorIcon from '@mui/icons-material/Error';
import {BackendSubjectType, Type, typeMapping} from '../utils/Interfaces';
import {useDispatch} from 'react-redux';
import {AppDispatch} from '../app/store';
import {deleteSubjectType} from '../app/slices/subjectTypeSlice';
import {useSnackbar} from 'notistack';
import TypeModal from "./TypeModal.tsx";
import AddIcon from "@mui/icons-material/Add";
import GroupsIcon from '@mui/icons-material/Groups';
import DashboardCustomizeIcon from '@mui/icons-material/DashboardCustomize';
import SubjectTypesTeachersList from "./SubjectTypesTeachersList.tsx";
import GroupsModal from './GroupsModal.tsx';
import SubjectTypesClassroomsModal from './SubjectTypesClassroomsModal.tsx';

interface SubjectTypesFormProps {
    subjectTypes: BackendSubjectType[];
    setSubjectTypes: React.Dispatch<React.SetStateAction<BackendSubjectType[]>>;
    semester: number;
    loading: boolean;
}

const SubjectTypesForm: React.FC<SubjectTypesFormProps> = ({subjectTypes, setSubjectTypes, semester, loading}) => {
    const dispatch = useDispatch<AppDispatch>();
    const {enqueueSnackbar} = useSnackbar();
    const [openTypeModal, setOpenTypeModal] = useState(false);
    const [openGroupsModal, setOpenGroupsModal] = useState(false);
    const [openClassroomsModal, setOpenClassroomsModal] = useState(false);
    const [openTeachersList, setOpenTeachersList] = useState(false);
    const [currentType, setCurrentType] = useState<BackendSubjectType | null>(null);

    const handleAdd = () => {
        setCurrentType(null);
        setOpenTypeModal(true);
    };

    const handleEdit = (type: BackendSubjectType) => {
        setCurrentType(type);
        setOpenTypeModal(true);
    };

    const handleTeachers = (type: BackendSubjectType) => {
        currentType === type ? setCurrentType(null) : setCurrentType(type);
        currentType === type ? setOpenTeachersList(false) : setOpenTeachersList(true);
    };

    const handleGroups = (type: BackendSubjectType) => {
        setCurrentType(type);
        setOpenGroupsModal(true);
    };

    const handleClassrooms = (type: BackendSubjectType) => {
        setCurrentType(type);
        setOpenClassroomsModal(true);
    };

    const handleDelete = async (id: number) => {
        try {
            await dispatch(deleteSubjectType(id)).unwrap();
            enqueueSnackbar('Typ przedmiotu usunięty!', {variant: 'success'});
            setSubjectTypes(prev => prev.filter(st => st.subjectTypeId !== id));
        } catch (error) {
            enqueueSnackbar(`Wystąpił błąd przy usuwaniu typu przedmiotu: ${error}`, {variant: 'error'});
        }
    };

    const handleSave = (typeData: BackendSubjectType, fromGroups: boolean) => {
        if (typeData.subjectTypeId) {
            setSubjectTypes(prev => prev.map(st => st.subjectTypeId === typeData.subjectTypeId ? typeData : st));
            enqueueSnackbar('Typ przedmiotu zaktualizowany!', {variant: 'success'});
        } else {
            if (fromGroups && typeData.frontId) {
                setSubjectTypes(prev => prev.map(st => st.frontId === typeData.frontId ? typeData : st));
                enqueueSnackbar('Typ przedmiotu zaktualizowany!', {variant: 'success'});
            } else {
                setSubjectTypes(prev => [...prev, {...typeData}]);
                enqueueSnackbar('Typ przedmiotu dodany!', {variant: 'success'});
            }
        }
        setOpenTypeModal(false);
    };

    const calcHours = (type: Type, numHours: number, numGroups: number) => {
        if(type === Type.LECTURE) return numHours;
        if(type === Type.EX) return numHours * (numGroups/2);
        return numHours * numGroups;
    }

    return (<Box>
            <TableContainer component={Paper} sx={{mt: 2}}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Typ</TableCell>
                            <TableCell>Licza godzin</TableCell>
                            <TableCell>Maks. studentów na grupę</TableCell>
                            <TableCell>Akcje</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {subjectTypes.map((type) => (<>
                                <TableRow key={type.type}
                                          style={currentType === type ? {backgroundColor: "#dddddd"} : {}}>
                                    <TableCell>{typeMapping[type.type]}</TableCell>
                                    <TableCell>{type.numOfHours}</TableCell>
                                    <TableCell>{type.maxStudentsPerGroup}</TableCell>
                                    <TableCell>
                                        <div className={"flex"}>
                                            <IconButton onClick={() => handleEdit(type)} disabled={loading}>
                                                <Edit/>
                                            </IconButton>
                                            <IconButton onClick={() => handleTeachers(type)} disabled={loading}>
                                                <PersonAddAlt/>
                                                {type.teachersList.reduce((sum, t) => {
                                                    return sum + t.numHours;
                                                }, 0) != calcHours(type.type, type.numOfHours, type.groupsList.length)  && (<ErrorIcon style={{
                                                    position: 'absolute',
                                                    top: -5,
                                                    right: -5,
                                                    fontSize: 20,
                                                    color: 'red'
                                                }}/>)}
                                            </IconButton>
                                            <IconButton onClick={() => handleGroups(type)} disabled={loading}>
                                                <GroupsIcon/>
                                                { type.groupsList.length === 0 && (<ErrorIcon style={{
                                                    position: 'absolute',
                                                    top: -5,
                                                    right: -5,
                                                    fontSize: 20,
                                                    color: 'red'
                                                }}/>)}
                                            </IconButton>
                                            <IconButton onClick={() => handleClassrooms(type)} disabled={loading}>
                                                <DashboardCustomizeIcon/>
                                            </IconButton>
                                            {/*TODO dodać zabezpieczenie pytające czy chcesz usunąć rekord <ConfirmationDialog>*/}
                                            {/*TODO Dodać możliwość usuwania rekordków które nie są jeszcze dodane - tj takie których jeszcze nie ma w bazie, bo główny przedmiot nie został dodany*/}
                                            <IconButton onClick={() => handleDelete(type.subjectTypeId!)}
                                                        disabled={loading}>
                                                <Delete/>
                                            </IconButton>
                                        </div>
                                    </TableCell>
                                </TableRow>
                                {currentType === type && openTeachersList && (<SubjectTypesTeachersList
                                    teachersList={currentType?.teachersList ? currentType.teachersList : []}
                                    typeData={currentType}
                                    maxHours={calcHours(type.type, type.numOfHours, type.groupsList.length)}
                                    setSubjectTypes={setSubjectTypes}
                                    loading={loading}
                                />)}
                            </>))}
                        {subjectTypes.length === 0 && (<TableRow>
                                <TableCell colSpan={4} align="center">
                                    Brak typów przedmiotów.
                                </TableCell>
                            </TableRow>)}
                    </TableBody>
                </Table>
            </TableContainer>
            <div className={"flex justify-center -mt-5"}>
                <div className={"bg-white"}>
                    <IconButton onClick={handleAdd} disabled={loading}>
                        <AddIcon/>
                    </IconButton>
                </div>
            </div>

            {openTypeModal && (<TypeModal
                    open={openTypeModal}
                    onClose={() => setOpenTypeModal(false)}
                    typeData={currentType}
                    onSave={handleSave}
                />)}
            {openGroupsModal && (<GroupsModal
                    open={openGroupsModal}
                    onClose={() => setOpenGroupsModal(false)}
                    typeData={currentType}
                    semester={semester}
                    onSave={handleSave}
                />)}
            {openClassroomsModal && (<SubjectTypesClassroomsModal
                open={openClassroomsModal}
                onClose={() => setOpenClassroomsModal(false)}
                typeData={currentType}
                onSave={handleSave}
            />)}
        </Box>);
};

export default SubjectTypesForm;

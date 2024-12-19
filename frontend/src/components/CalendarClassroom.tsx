import {FC, useMemo} from 'react';
import {Paper, Table, TableBody, TableCell, TableHead, TableRow} from '@mui/material';
import {BackendClassroom, Day} from '../utils/Interfaces';

interface ClusterData {
    key: {
        day: Day;
        timeRange: string;
        subjectName: string;
        teacher: string;
        classroom: number;
        isEvenWeek: boolean | null;
        subjectType: string;
    };
    groupCodes: string[];
}

interface ClassroomTableProps {
    clusters: ClusterData[];
    allClassrooms: BackendClassroom[];
    subjectColorMap: { [subject: string]: string };
    dayMapping: { [key in Day]: string };
    dayToIndex: { [key in Day]: number };
}

const ClassroomTable: FC<ClassroomTableProps> = ({
                                                     clusters, allClassrooms, subjectColorMap, dayMapping, dayToIndex,
                                                 }) => {
    const sortedClassrooms = useMemo(() => {
        return [...allClassrooms].sort((a, b) => a.classroomID!.toString().localeCompare(b.classroomID!.toString()));
    }, [allClassrooms]);

    const dayMap = useMemo(() => {
        const map = new Map<Day, ClusterData[]>();
        clusters.forEach((c) => {
            if (!map.has(c.key.day)) {
                map.set(c.key.day, []);
            }
            map.get(c.key.day)!.push(c);
        });
        return map;
    }, [clusters]);

    const sortedDays = useMemo(() => {
        return Array.from(dayMap.keys()).sort((a, b) => dayToIndex[a] - dayToIndex[b]);
    }, [dayMap, dayToIndex]);

    return (<Paper className="overflow-x-auto overflow-auto pt-2">
        <Table stickyHeader className="table-fixed" sx={{borderCollapse: 'collapse'}}>
            <TableHead>
                <TableRow>
                    <TableCell className="w-16 text-center text-xs p-1">Dzień</TableCell>
                    <TableCell className="w-24 text-center text-xs p-1">Czas</TableCell>
                    {sortedClassrooms.map((classroom) => {
                        const s = `${classroom.code} (${classroom.building.code})`;
                        return (<TableCell
                            key={classroom.classroomID}
                            className="w-32 text-center text-xs p-1 border-r border-gray-300"
                        >
                            {s}
                        </TableCell>);
                    })}
                </TableRow>
            </TableHead>
            <TableBody>
                {sortedDays.map((day) => {
                    const dayClusters = dayMap.get(day)!;
                    const timeMap = new Map<string, ClusterData[]>();

                    dayClusters.forEach((cluster) => {
                        const tr = cluster.key.timeRange;
                        if (!timeMap.has(tr)) {
                            timeMap.set(tr, []);
                        }
                        timeMap.get(tr)!.push(cluster);
                    });

                    const sortedTimeRanges = Array.from(timeMap.keys()).sort((a, b) => {
                        const [aStart] = a.split('-');
                        const [bStart] = b.split('-');
                        return aStart.localeCompare(bStart);
                    });

                    return sortedTimeRanges.map((timeRange, trIdx) => (<TableRow key={`${day}-${timeRange}-${trIdx}`}>
                        {trIdx === 0 && (<TableCell
                            rowSpan={sortedTimeRanges.length}
                            className="relative align-middle text-center font-bold border-0 p-0 whitespace-nowrap text-xs"
                        >
                            <div
                                className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 -rotate-90">
                                {dayMapping[day]}
                            </div>
                        </TableCell>)}
                        <TableCell className="align-middle text-center p-0 text-xs">
                            {timeRange}
                        </TableCell>
                        {sortedClassrooms.map((classroom) => {
                            const cluster = timeMap.get(timeRange)?.find((cl) => cl.key.classroom === classroom.classroomID!);
                            if (!cluster) {
                                return (<TableCell
                                    key={`empty-${day}-${timeRange}-${classroom.classroomID}`}
                                    className="p-0 border-r border-gray-300"
                                />);
                            }

                            const {subjectName, subjectType, teacher, isEvenWeek} = cluster.key;

                            return (<TableCell
                                key={`cluster-${day}-${timeRange}-${classroom.classroomID}`}
                                className="align-middle p-0 border-r border-gray-300"
                                style={{
                                    backgroundColor: subjectColorMap[subjectName] || '#fff',
                                }}
                            >
                                <div
                                    className="flex flex-col h-full justify-center items-center text-center text-xs p-0">
                                    <div className="font-bold text-xs">{subjectName}</div>
                                    <div className="text-xs">{subjectType}</div>
                                    <div className="text-xs">
                                        {teacher}
                                        {isEvenWeek !== null && (<>
                                            <br/>
                                            {isEvenWeek ? 'Tydzień parzysty' : 'Tydzień nieparzysty'}
                                        </>)}
                                    </div>
                                </div>
                            </TableCell>);
                        })}
                    </TableRow>));
                })}
            </TableBody>
        </Table>
    </Paper>);
};

export default ClassroomTable;

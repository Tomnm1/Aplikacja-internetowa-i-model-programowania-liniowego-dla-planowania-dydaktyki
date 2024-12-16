import {FC} from 'react';
import {Paper, Table, TableBody, TableCell, TableHead, TableRow,} from '@mui/material';
import {Day} from '../utils/Interfaces';

interface ClusterData {
    key: {
        day: Day;
        timeRange: string;
        subjectName: string;
        teacher: string;
        classroom: string;
        isEvenWeek: boolean | null;
        subjectType: string;
    };
    groupCodes: string[];
}

interface SemesterTableViewProps {
    clusters: ClusterData[];
    allGroupCodes: string[];
    subjectColorMap: { [subject: string]: string };
    dayMapping: { [key in Day]: string };
    dayToIndex: { [key in Day]: number };
}

const CalendarTable: FC<SemesterTableViewProps> = ({
                                                       clusters, allGroupCodes, subjectColorMap, dayMapping, dayToIndex,
                                                   }) => {
    const sortedGroupCodes = [...allGroupCodes].sort((a, b) => {
        const aNum = parseInt(a.replace(/\D+/g, ''), 10);
        const bNum = parseInt(b.replace(/\D+/g, ''), 10);
        return (aNum - bNum) || a.localeCompare(b);
    });

    const dayMap = new Map<Day, ClusterData[]>();
    clusters.forEach((c) => {
        if (!dayMap.has(c.key.day)) {
            dayMap.set(c.key.day, []);
        }
        dayMap.get(c.key.day)!.push(c);
    });
    const sortedDays = Array.from(dayMap.keys()).sort((a, b) => dayToIndex[a] - dayToIndex[b]);

    return (<Paper className="overflow-x-auto overflow-auto">
            <Table stickyHeader className="table-fixed w-full">
                <TableHead>
                    <TableRow>
                        <TableCell className="w-[40px] text-center text-xs p-1">
                            Dzień
                        </TableCell>
                        <TableCell className="w-[60px] text-center text-xs p-1">
                            Czas
                        </TableCell>
                        {sortedGroupCodes.map((groupCode, idx) => (<TableCell
                                key={groupCode}
                                className={`w-[80px] text-center text-xs p-1 ${idx === sortedGroupCodes.length - 1 ? '' : 'border-r border-gray-300'}`}
                            >
                                {groupCode}
                            </TableCell>))}
                    </TableRow>
                </TableHead>
                <TableBody>
                    {sortedDays.map((d) => {
                        const dayClusters = dayMap.get(d)!;

                        const timeMap = new Map<string, ClusterData[]>();
                        dayClusters.forEach((cl) => {
                            const tr = cl.key.timeRange;
                            if (!timeMap.has(tr)) {
                                timeMap.set(tr, []);
                            }
                            timeMap.get(tr)!.push(cl);
                        });

                        const sortedTimeRanges = Array.from(timeMap.keys()).sort((a, b) => {
                            const [aStart] = a.split('-');
                            const [bStart] = b.split('-');
                            return aStart.localeCompare(bStart);
                        });

                        const dayRowCount = sortedTimeRanges.length;

                        return sortedTimeRanges.map((tr, trIdx) => {
                            const trClusters = timeMap.get(tr)!;

                            const rowCells: JSX.Element[] = [];

                            sortedGroupCodes.forEach((groupCode, groupIdx) => {
                                const clustersForGroup = trClusters.filter((cl) => cl.groupCodes.includes(groupCode));

                                if (clustersForGroup.length === 0) {
                                    rowCells.push(<TableCell
                                        key={`empty-${d}-${tr}-${groupIdx}`}
                                        className="p-1"
                                    />);
                                } else if (clustersForGroup.length === 1) {
                                    const cl = clustersForGroup[0];
                                    const bgColor = subjectColorMap[cl.key.subjectName] || '#fff';
                                    rowCells.push(<TableCell
                                        key={`cluster-${d}-${tr}-${groupIdx}`}
                                        className="align-middle p-1"
                                        style={{
                                            backgroundColor: bgColor,
                                        }}
                                        rowSpan={trClusters.length > 1 ? 1 : undefined}
                                    >
                                        <div
                                            className="flex flex-col h-full justify-center items-center text-center text-xs overflow-hidden">
                                            <div className="font-bold text-sm">
                                                {cl.key.subjectName}
                                            </div>
                                            <div className="text-xs">
                                                {cl.key.subjectType}
                                            </div>
                                            <div className="text-xs">
                                                {cl.key.teacher}
                                                {cl.key.isEvenWeek !== null && (<>
                                                        <br/>
                                                        {cl.key.isEvenWeek ? 'Tydzień parzysty' : 'Tydzień nieparzysty'}
                                                    </>)}
                                            </div>
                                            <div className="font-bold text-xs mt-0.5">
                                                {cl.key.classroom}
                                            </div>
                                        </div>
                                    </TableCell>);
                                } else {
                                    rowCells.push(<TableCell
                                        key={`multi-cluster-${d}-${tr}-${groupIdx}`}
                                        className="p-1"
                                    >
                                        <div className="flex flex-col h-full">
                                            {clustersForGroup.map((cl, idx) => {
                                                const bgColor = subjectColorMap[cl.key.subjectName] || '#fff';
                                                return (<div
                                                        key={`sub-cluster-${idx}`}
                                                        className="flex-1 flex flex-col justify-center items-center text-center text-xs overflow-hidden border-b last:border-b-0"
                                                        style={{
                                                            backgroundColor: bgColor,
                                                        }}
                                                    >
                                                        <div className="font-bold text-sm">
                                                            {cl.key.subjectName}
                                                        </div>
                                                        <div className="text-xs">
                                                            {cl.key.subjectType}
                                                        </div>
                                                        <div className="text-xs">
                                                            {cl.key.teacher}
                                                            {cl.key.isEvenWeek !== null && (<>
                                                                    <br/>
                                                                    {cl.key.isEvenWeek ? 'Tydzień parzysty' : 'Tydzień nieparzysty'}
                                                                </>)}
                                                        </div>
                                                        <div className="font-bold text-xs mt-0.5">
                                                            {cl.key.classroom}
                                                        </div>
                                                    </div>);
                                            })}
                                        </div>
                                    </TableCell>);
                                }
                            });

                            return (<TableRow key={`${d}-${tr}-${trIdx}`}>
                                    {trIdx === 0 && (<TableCell
                                            rowSpan={dayRowCount}
                                            className="align-middle text-center font-bold border-0 p-1 whitespace-nowrap text-xs [transform:rotate(-90deg)]"
                                        >
                                            {dayMapping[d]}
                                        </TableCell>)}
                                    <TableCell className="align-middle text-center p-1 text-xs">
                                        {tr}
                                    </TableCell>
                                    {rowCells}
                                </TableRow>);
                        });
                    })}
                </TableBody>
            </Table>
        </Paper>);
};

export default CalendarTable;

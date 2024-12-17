import {FC} from 'react';
import {Paper, Table, TableBody, TableCell, TableHead, TableRow} from '@mui/material';
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
                                                       clusters, allGroupCodes, subjectColorMap, dayMapping, dayToIndex
                                                   }) => {
    allGroupCodes.sort((a, b) => {
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
    return(
        <Paper className="overflow-x-auto overflow-auto">
        <Table
            stickyHeader
            className="table-fixed w-full"
        >
            <TableHead>
                <TableRow>
                    <TableCell
                        className="w-[40px] text-center text-xs p-1"
                    >
                        Dzień
                    </TableCell>
                    <TableCell
                        className="w-[60px] text-center text-xs p-1"
                    >
                        Czas
                    </TableCell>
                    {allGroupCodes.map((groupCode, idx) => (<TableCell
                        key={groupCode}
                        className={`w-[80px] text-center text-xs p-1 ${idx === allGroupCodes.length - 1 ? '' : 'border-r border-gray-300'}`}
                    >
                        {groupCode}
                    </TableCell>))}
                </TableRow>
            </TableHead>
            <TableBody>
                {sortedDays.map((d) => {
                    const dayClusters = dayMap.get(d)!;

                    const timeMap = new Map<string, ClusterData[]>();
                    dayClusters.forEach((cl: any) => {
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

                        const clusterCells: any[] = [];
                        trClusters.forEach((cl: any) => {
                            const gIndexes = cl.groupCodes.map((g: string) => allGroupCodes.indexOf(g));
                            gIndexes.sort((a: number, b: number) => a - b);
                            const minIdx = Math.min(...gIndexes);
                            const maxIdx = Math.max(...gIndexes);
                            const colSpan = maxIdx - minIdx + 1;
                            console.log(cl.key.subjectName, cl.key.isEvenWeek)
                            clusterCells.push({
                                minIdx,
                                maxIdx,
                                colSpan,
                                subjectName: cl.key.subjectName,
                                subjectType: cl.key.subjectType,
                                teacher: cl.key.teacher,
                                classroom: cl.key.classroom,
                                isEvenWeek: cl.key.isEvenWeek
                            });
                        });

                        clusterCells.sort((a, b) => a.minIdx - b.minIdx);

                        const rowCells: JSX.Element[] = [];
                        let currentIndex = 0;
                        clusterCells.forEach((cObj, i) => {
                            while (currentIndex < cObj.minIdx) {
                                rowCells.push(<TableCell
                                    key={`empty-bef-${currentIndex}`}
                                    className="p-1"
                                />);
                                currentIndex++;
                            }
                            const bgColor = subjectColorMap[cObj.subjectName] || '#fff';
                            rowCells.push(<TableCell
                                key={`cluster-${cObj.minIdx}-${i}`}
                                colSpan={cObj.colSpan}
                                className="align-middle p-1"
                                style={{backgroundColor: bgColor}}
                            >
                                <div
                                    className="flex flex-col items-center text-center p-0.5 leading-tight text-xs overflow-hidden text-ellipsis">
                                    <div className="font-bold text-sm">
                                        {cObj.subjectName}
                                    </div>
                                    <div className="text-xs">
                                        {cObj.subjectType}
                                    </div>
                                    <div className="text-xs">
                                        {cObj.teacher}
                                        {cObj.isEvenWeek !== null && (<>
                                            <br/>
                                            {cObj.isEvenWeek ? 'Tydzień parzysty' : 'Tydzień nieparzysty'}
                                        </>)}
                                    </div>
                                    <div className="font-bold text-xs mt-0.5">
                                        {cObj.classroom}
                                    </div>
                                </div>
                            </TableCell>);
                            currentIndex += cObj.colSpan;
                        });

                        while (currentIndex < allGroupCodes.length) {
                            rowCells.push(<TableCell
                                key={`empty-aft-${currentIndex}`}
                                className="p-1"
                            />);
                            currentIndex++;
                        }
                        return (<TableRow
                            key={`${d}-${tr}-${trIdx}`}
                        >
                            {trIdx === 0 && (<TableCell
                                rowSpan={dayRowCount}
                                className="align-middle text-center font-bold border-0 p-1 whitespace-nowrap text-xs [transform:rotate(-90deg)]"
                            >
                                {dayMapping[d]}
                            </TableCell>)}
                            <TableCell
                                className="align-middle text-center p-1 text-xs"
                            >
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

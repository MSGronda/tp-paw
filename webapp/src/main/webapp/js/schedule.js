class Schedule {
    constructor(rows, cols) {
        this.scheduleHTML = document.getElementById('weekly-schedule');
        this.rows = rows;
        this.cols = cols;
        this.scheduleArray = [];
        this.chosenSubjectMap = {};

        this.colors = [
            '#fda4a5', '#fdba74' ,'#fce046', '#bff265',
            '#86eead', '#5febd4', '#7dd2fd', '#a4b5fc'
        ]
        this.currentColor = 0

        // we must initialize it this way :(
        for(let i=0; i<rows; i++ ){
            this.scheduleArray[i] = []
            for(let j=0; j<cols; j++){
                this.scheduleArray[i][j] = 0;
            }
        }
        this.setupSchedule();
    }
    normalizeArrayPos(max, val){
        return Math.max(Math.min(max - 1, val), 0)
    }

    setupSchedule(){
        for(let i=0; i<this.rows; i++ ){
            let tr = document.createElement("tr");

            // set first column to time. Example: 8:00
            let td = document.createElement('th');
            let hours = (8 + Math.floor(i / 2)) + "";
            let minutes = i % 2 === 0 ? "00" : "30";
            td.textContent = hours + ":" + minutes;
            tr.appendChild(td);

            for(let j=1; j<this.cols; j++ ){
                let td = document.createElement('td');
                td.id='r'+i+'c'+j;
                this.scheduleArray[i][j] = 0;
                td.textContent = "";
                tr.appendChild(td);
            }
            this.scheduleHTML.appendChild(tr);
        }
    }
    nextColor(){
        this.currentColor = (this.currentColor + 1) % this.colors.length
    }

    getTime(time){
        if(time === '')
            return -1
        return (parseInt(time.split(":")[0])-8)*2 + (parseInt(time.split(":")[1]) === 30 ? 1 : 0);
    }

    getArrayPos(classTime){
        const day = parseInt(classTime.day)
        const start = classTime.start
        const end = classTime.end

        const rowStart = this.getTime(start)
        const rowEnd = this.getTime(end)
        return {'rowStart': rowStart, 'rowEnd': rowEnd, 'column':day};
    }

    canAddSubject(subjectId){
        // disallow user that has already signed up for that class
        return !this.chosenSubjectMap.hasOwnProperty(subjectId);
    }

    canAddClass(classTimes){
        // disallow user that has time slot taken
        for(let eventNum in classTimes){
            const arrayPos = this.getArrayPos(classTimes[eventNum])

            if(arrayPos.rowStart === -1 || arrayPos.rowEnd === -1)
                continue;

            for(let i=arrayPos.rowStart; i<arrayPos.rowEnd; i++){

                if(this.scheduleArray[this.normalizeArrayPos(this.rows, i)][this.normalizeArrayPos(this.cols , arrayPos.column)]!==0){
                    return false;
                }
            }
        }
        return true;
    }
    signedUpToClass(subjectId){
        return this.chosenSubjectMap.hasOwnProperty(subjectId)
    }
    generateEventInfo(subjectId,subjectName, classTime){
        const div = document.createElement('div');
        div.classList.add('column-center')

        const id = document.createElement('div');
        id.textContent = subjectName
        id.style.maxWidth = '6rem';
        id.style.textAlign = 'center';
        id.style.alignSelf = 'center';
        id.style.fontWeight = 'bold';

        const loc = document.createElement('div');
        loc.textContent = classTime.loc

        const mode = document.createElement('div');
        mode.textContent = classTime.mode

        div.append(id,loc,mode)

        return div;
    }

    addToCalendarHtml(rowStart, rowEnd, column, subjectId,subjectName, classTime){
        column = this.normalizeArrayPos(this.cols, column);
        rowStart = this.normalizeArrayPos(this.rows, rowStart);
        rowEnd = this.normalizeArrayPos(this.rows, rowEnd);
        
        const elem = document.getElementById('r'+rowStart+'c'+column)
        elem.rowSpan = rowEnd - rowStart
        elem.style.backgroundColor = this.colors[this.currentColor]


        elem.append(this.generateEventInfo(subjectId,subjectName,classTime))

        for(let i=rowStart+1; i<rowEnd; i++){
            document.getElementById('r'+i+'c'+column).style.display = 'none';
        }
    }
    addToCalendarArray(rowStart, rowEnd, column, subjectId){
        for(let i=rowStart; i<rowEnd; i++){
            this.scheduleArray[this.normalizeArrayPos(this.rows , i)][this.normalizeArrayPos(this.cols , column)] = subjectId;
        }
    }
    addClass(subjectId, subjectName, classTimes){
        if(!this.canAddSubject(subjectId) ||  !this.canAddClass(classTimes)) {
            return false;
        }
        this.chosenSubjectMap[subjectId] = classTimes;

        for(let eventNum in classTimes){
            const arrayPos = this.getArrayPos(classTimes[eventNum])

            if(arrayPos.rowStart === -1 || arrayPos.rowEnd === -1)
                continue;

            this.addToCalendarHtml(arrayPos.rowStart, arrayPos.rowEnd, arrayPos.column, subjectId, subjectName, classTimes[eventNum])
            this.addToCalendarArray(arrayPos.rowStart, arrayPos.rowEnd, arrayPos.column, subjectId)
        }
        this.nextColor()

        return true;
    }


    removeFromCalendarHtml(rowStart, rowEnd, column){
        const elem = document.getElementById('r'+rowStart+'c'+column)
        elem.style.backgroundColor = '#ffffff';
        elem.removeAttribute('rowSpan')
        elem.textContent = '';

        for(let i=rowStart+1; i<rowEnd; i++){
            // careful with this
            document.getElementById('r'+i+'c'+column).removeAttribute('style')
        }
    }
    removeFromCalendarArray(rowStart, rowEnd, column){
        for(let i=rowStart; i<rowEnd; i++){
            this.scheduleArray[this.normalizeArrayPos(this.rows, i)][this.normalizeArrayPos(this.cols, column)] = 0;
        }
    }
    removeClass(subjectId){
        for(let elem in this.chosenSubjectMap[subjectId]){
            const arrayPos = this.getArrayPos(this.chosenSubjectMap[subjectId][elem])

            if(arrayPos.rowStart === -1 || arrayPos.rowEnd === -1)
                continue;

            this.removeFromCalendarHtml(arrayPos.rowStart,arrayPos.rowEnd,arrayPos.column)

            this.removeFromCalendarArray(arrayPos.rowStart,arrayPos.rowEnd,arrayPos.column)
        }
        delete this.chosenSubjectMap[subjectId]
    }

    generateCsv(){
        const csvData = [];

        // day of weak beader
        csvData.push([' ',daysOfWeek[0],daysOfWeek[1],daysOfWeek[2],daysOfWeek[3],daysOfWeek[4],daysOfWeek[5]].join(',') )

        const table = document.getElementById('weekly-schedule')

        for(let row in this.scheduleArray){
            const rowData = [];
            // time
            rowData.push(table.children[row].children[0].innerHTML)

            for(let col in this.scheduleArray[row]){
                if(this.scheduleArray[row][col] === 0)
                    rowData.push(' ');
                else
                    rowData.push(this.scheduleArray[row][col])
            }
            csvData.push(rowData.join(','));
        }
        return csvData.join('\n');
    }


}
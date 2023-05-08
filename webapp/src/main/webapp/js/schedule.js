class Schedule {
    constructor(rows, cols) {
        this.scheduleHTML = document.getElementById('weekly-schedule');
        this.rows = rows;
        this.cols = cols;
        this.scheduleArray = [];
        this.chosenSubjectMap = {};

        // we must initialize it this way :(
        for(let i=0; i<rows; i++ ){
            this.scheduleArray[i] = []
            for(let j=0; j<cols; j++){
                this.scheduleArray[i][j] = 0;
            }
        }
        this.setupSchedule();
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


    canAddClass(subjectId, classTimes){
        // disallow user that has already signed up for that class
        if(this.chosenSubjectMap.hasOwnProperty(subjectId)){
            return false;
        }

        // disallow user that has time slot taken
        for(let eventNum in classTimes){
            const day = parseInt(classTimes[eventNum].day)
            const start = classTimes[eventNum].start
            const end = classTimes[eventNum].end

            const rowStart = (parseInt(start.split(":")[0])-8)*2 + (parseInt(start.split(":")[1]) === 30 ? 1 : 0)
            const rowEnd = (parseInt(end.split(":")[0])-8)*2 + (parseInt(end.split(":")[1]) === 30 ? 1 : 0)

            for(let i=rowStart; i<rowEnd; i++){
                if(this.scheduleArray[i][day]!==0){
                    return false;
                }
            }
        }
        return true;
    }
    updateCalendarHtml(row, column){
        document.getElementById('r'+row+'c'+column).style.backgroundColor = '#8d2f2f';
    }

    addClass(subjectId,classTimes){
        if(!this.canAddClass(subjectId,classTimes)) {
            return false;
        }
        this.chosenSubjectMap[subjectId] = classTimes;

        for(let eventNum in classTimes){
            const day = parseInt(classTimes[eventNum].day)
            const start = classTimes[eventNum].start
            const end = classTimes[eventNum].end

            const rowStart = (parseInt(start.split(":")[0])-8)*2 + (parseInt(start.split(":")[1]) === 30 ? 1 : 0)
            const rowEnd = (parseInt(end.split(":")[0])-8)*2  + (parseInt(end.split(":")[1]) === 30 ? 1 : 0)

            for(let i=rowStart; i<rowEnd; i++){
                this.updateCalendarHtml(i,day)
                this.scheduleArray[i][day] = subjectId;
            }
        }

        return true;
    }
}
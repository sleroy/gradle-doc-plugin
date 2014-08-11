## /cyfe

### /value/{kpiKey}/{entityKey}

    CSV file for displaying a simple kpi value in Cyfe 
    with a gauge if the parameter 'goal' is not null
    
    Method: GET
    Response: text/csv;charset=UTF-8
    Parameters:
        - entityKey: String
        - timescale: TimeScale (optional)
        - date: yyyy-MM-dd (optional)
        - goal: Double (optional)


### /values

    CSV file for displaying a table in Cyfe 
    with values from multiples kpis and multiple entities
    
    Method: GET
    Response: text/csv;charset=UTF-8
    Parameters:
        - kpiKeys: List<String>
        - entityKeys: List<String> (optional)
        - timescale: TimeScale (optional)
        - date: yyyy-MM-dd (optional)


### /serie/{kpiKey}

    CSV file for displaying a graph in Cyfe 
    with kpi values over a period of time
    
    Method: GET
    Response: text/csv;charset=UTF-8
    Parameters:
        - entityKeys: List<String> (optional)
        - timescale: TimeScale (optional)
        - since: yyyy-MM-dd (optional)
        - colors: List<String> (optional)
        - types: List<String> (optional)


### /pie/{kpiKey}

    CSV file for displaying a piechart or a donut graph in Cyfe 
    with the values of a kpi
    
    Method: GET
    Response: text/csv;charset=UTF-8
    Parameters:
        - entityKeys: List<String> (optional)
        - timescale: TimeScale (optional)
        - colors: List<String> (optional)


### /cohort/{kpiKey}

    CSV file for displaying a cohort table in Cyfe 
    with the values of a kpi
    
    Method: GET
    Response: text/csv;charset=UTF-8
    Parameters:
        - entityKeys: List<String> (optional)
        - timescale: TimeScale (optional)
        - since: yyyy-MM-dd (optional)
        - goal: Double (optional)
        - color: String (optional)


### /events/{entityType}

    CSV file for displaying a list of events in a table in Cyfe 
    
    Method: GET
    Response: text/csv;charset=UTF-8
    Parameters:
        - severityMin: Severity 
        - sizeMax: Integer
        - eventKeys: List<String> (optional)
        - entityKeys: List<String> (optional)

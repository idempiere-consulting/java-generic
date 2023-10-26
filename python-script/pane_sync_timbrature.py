import pyodbc
import requests
import json
import datetime

body = {
        "userName": "mobile",
        "password": "test123",
        "parameters": {
            "clientId": 1000000,
            "roleId": 1000000,
            "organizationId": 1000000,
            "warehouseId": 1000000,
            "language": "it_IT"
        }
    }

header = {  'Content-Type': 'application/json' }

r = requests.post('https://ilpanequotidiano.idempiere.it/api/v1/auth/tokens', data=json.dumps(body), headers=header)

if r.status_code == 200:
    jsonRes = json.loads(r.text)
    token = jsonRes["token"]


    conn = pyodbc.connect('Driver={SQL Server};'
                      'Server=SRV-TIMB\SOLARI;'
                      'Database=DBStart;'
                      'UID=utente;'
                      'PWD=Utente123!')

    cursor = conn.cursor()
    cursor.execute('SELECT * FROM TIMBRATURE WHERE cast(DATAV as Date) = cast(getdate() as Date);')
    # WHERE cast(DATAV as Date) = cast(getdate() as Date)
    rows = cursor.fetchall()
    for row in rows:
        r = requests.get('https://ilpanequotidiano.idempiere.it/api/v1/models/LIT_Hour?$filter= lit_idExternal eq '+ '\''+ str(row.IDTIMBRATURA)+ '\'', headers={  'Content-Type': 'application/json', 'Authorization': 'Bearer '+token })
        if r.status_code == 200:
            resJson = json.loads(r.text)
            #print(resJson["row-count"])
            if resJson["row-count"] == 0:
                print(row)
                #print(row.DATAV.year)
                datav = ''
                datao = ''
                try:
                    datav = row.DATAV.strftime("%Y-%m-%dT%H:%M:%SZ")
                except:
                    datav = ''
                try:
                    datao = row.DATAO.strftime("%Y-%m-%dT%H:%M:%SZ")
                except:
                    datao = ''
                #print(row.DATAV.strftime("%Y-%m-%dT%H:%M:%S:00Z"))
                p = requests.post('https://ilpanequotidiano.idempiere.it/api/v1/models/LIT_Hour/',data=json.dumps({ 'AD_Client_ID': 1000000, 'AD_Org_ID': 1000000, 'DateStart': datav,'EndDate': datao, 'lit_HRTerminal': str(row.IDTERMINALE) , 'LIT_BadgeEmployee': str(row.BADGE), 'lit_idExternal': row.IDTIMBRATURA ,'LIT_HourExtEmployeeNr': str(row.IDDIP)}), headers={  'Content-Type': 'application/json', 'Authorization': 'Bearer '+token })
                #print(p.url)
                print(str(p.text))
                if p.status_code == 200:
                    print("done!")


    cursor.execute('SELECT * FROM VALORIVOCI WHERE cast(DATA as Date) > cast(getdate() -90 as Date);')
    # WHERE cast(DATA as Date) = cast(getdate() as Date)
    rows = cursor.fetchall()
    for row in rows:
        r = requests.get('https://ilpanequotidiano.idempiere.it/api/v1/models/lit_hoursummary?$filter= lit_idExternal eq '+ '\''+ str(row.LOGID)+ '\'', headers={  'Content-Type': 'application/json', 'Authorization': 'Bearer '+token })
        print(r.text)
        if r.status_code == 200:
            #print('hello')
            resJson = json.loads(r.text)
            #print(resJson["row-count"])
            if resJson["row-count"] == 0:
                print(row)
                #print(row.DATAV.year)
                #print(row.DATAV.strftime("%Y-%m-%dT%H:%M:%S:00Z"))
                p = requests.post('https://ilpanequotidiano.idempiere.it/api/v1/models/lit_hoursummary/',data=json.dumps({ 'AD_Client_ID': 1000000, 'AD_Org_ID': 1000000, 'DateDoc': row.DATA.strftime("%Y-%m-%d"), 'lit_idExternal': str(row.LOGID) , 'Qty': row.VALORE, 'lit_IDExtEmployeeHour': row.IDDIP,'lit_hourtype_ID': str(row.IDVOCE),}), headers={  'Content-Type': 'application/json', 'Authorization': 'Bearer '+token })
                print(p.url)
                print('Code '+str(p.text))
                if p.status_code == 200:
                    print("done!")

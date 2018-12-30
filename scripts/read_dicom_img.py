import pydicom as dicom

path = 'C:\\Users\\MariusI\\AppData\\Local\Temp\\tempcollection\\image6440297503330535228.dcm'
ds = dicom.dcmread(path)
print(ds.PatientName)


# Workflow ORA

## Image Preparation
  1. Open tiff-file with GIMP.
  2. Crop image
  3. Change ?Mode? to ?Grayscale? (Image Menu)
  4. Change ?Genauigkeit? to ?16-Bit Ganzzahl? (Wahrgenommenes Gamma sRGB)
  5. ?Farbprofil zuweisen?: GIMP built-in D65 Grayscale with sRGB TRC
  6. ?Farbverwaltung aktivieren? checked
  7. Datei -> Exportieren als
    a. Kompression: keine
    b. Checked: nur ?Save color profile?
  8. Skala vermessen
    a. X: 27,5; y: 11,4
    b. X: 26,4; y: 9,4

## ORA
  1. OCT format: X scale 10; Y scale 4
  2. LRP Width: 25
  3. LRP Height: 300
  4. #LRP: 1
  5. LPR spacing: 300 microns
  6. LRP Smoothing: 0
  7. Generate Anchor LRP: Manual
    a. Click at the x position of the foveal pit, roughly center y position in image
    b. Adjust positon of anchor manually
  8. Mark EZ and RPE on LRP curve
  9. Create Screenshot of LRP curve: Shift-Cmd-4-space; point camera to window

## Documentation
  1. Create Excel table from patient list, including patient ID, date of exam and side
  2. Document Names of raw image and of grayscale image in table
  3. Document Reflectivities of EZ and RPE in table

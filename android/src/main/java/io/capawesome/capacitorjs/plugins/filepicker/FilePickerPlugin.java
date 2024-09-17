package io.capawesome.capacitorjs.plugins.filepicker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import androidx.annotation.Nullable;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Logger;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@CapacitorPlugin(name = "FilePicker")
public class FilePickerPlugin extends Plugin {

    public static final String TAG = "FilePickerPlugin";

    public static final String ERROR_PICK_FILE_FAILED = "pickFiles failed.";
    public static final String ERROR_PICK_FILE_CANCELED = "pickFiles canceled9.";
    private FilePicker implementation;

    private String dataToSave;

    public void load() {
        implementation = new FilePicker(this.getBridge());
    }

    @PluginMethod
    public void convertHeicToJpeg(PluginCall call) {
        call.unimplemented("Not implemented on Android.");
    }



// Log.w("chooseFile", "uri_filter:" + uri_filter);

//         // type and title should be configurable

// //#########################
//         if( uri_filter.equals("image/jpeg") ) {
//             Log.w("File URI:", file_uri);
//             try {
//                 Uri uri = Uri.parse(file_uri);
//                 ParcelFileDescriptor pfd = this.cordova.getActivity().getContentResolver().
//                         openFileDescriptor(uri, "w");
//                 FileOutputStream fileOutputStream =
//                         new FileOutputStream(pfd.getFileDescriptor());
//                 fileOutputStream.write(file_data.getBytes());
//                 // Let the document provider know you're done by closing the stream.
//                 fileOutputStream.close();
//                 pfd.close();
//                 callback.success("Mapa salvo com sucesso!");
//             } catch (FileNotFoundException e) {
//                 e.printStackTrace();
//                 callback.error("ERRO FILE NOT FOUND!");
//             } catch (IOException e) {
//                 e.printStackTrace();
//                 callback.error("ERRO EXCEPTION!");
//             }
//         }
//         else if( uri_filter == "*/*") {
//             Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//             Intent chooser = Intent.createChooser(intent, "Selecionar Pasta");
//             cordova.startActivityForResult(this, chooser, RQS_OPEN_DOCUMENT_TREE);
//         } else if(  uri_filter.equals("application/vnd.ms-excel") ) {
//             // Usado para pegar o intent!!
//             final Intent intent = this.cordova.getActivity().getIntent();
//             final String action = intent.getAction();

//             if(Intent.ACTION_VIEW.equals(action)){
//                 //uri = intent.getStringExtra("URI");
//                 Uri uri2 = intent.getData();
//                 //String uri = uri2.getEncodedPath() + "  complete: " + uri2.toString();
//                 String uri = uri2.toString();
//                 //TextView textView = (TextView)findViewById(R.id.textView);
//                 //textView.setText(uri);
//                 // now you call whatever function your app uses
//                 // to consume the txt file whose location you now know
//                 Log.d(TAG, "intent was something else: "+action);
//                 callbackContext.success(uri);
//             } else {
//                 Log.d(TAG, "intent was something else: "+action);
//                 callbackContext.success("");
//             }



//         }
//         else if(  uri_filter.equals("application/pdf") ) {

//             Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
//             intent.addCategory(Intent.CATEGORY_OPENABLE);
//             intent.setType("application/x-freemind");//application/vnd.ms-project
// //            String def_name = "";
// //            Uri uri = Uri.parse(file_uri);
// //            List<String> strl = uri.getPathSegments();
// //            String name = intent.getData().getLastPathSegment();
// //            String name2 = intent.getData().getPath();
// //            String name3 = intent.getData().getEncodedPath();

//             intent.putExtra(Intent.EXTRA_TITLE, "");

//             Intent chooser = Intent.createChooser(intent, "Salvar Mapa");
//             cordova.startActivityForResult(this, chooser, RQS_SAVE_FILE);


//         } else if(  uri_filter.equals("text/plain") ) {
//             uri_filter = "*/*";
//             Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//             intent.setType(uri_filter);
//             intent.addCategory(Intent.CATEGORY_OPENABLE);
//             intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//             Intent chooser = Intent.createChooser(intent, "Escolher Mapa");
//             cordova.startActivityForResult(this, chooser, RQS_OPEN_DOCUMENT_SHARE);
//         } else {
//             uri_filter = "*/*";
//         //###############

//         //        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//         //        intent.addCategory(Intent.CATEGORY_OPENABLE);
//         //        intent.setType("application/pdf");
//         //
//         //
//         //        // Optionally, specify a URI for the file that should appear in the
//         //        // system file picker when it loads.
//         //        //intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
//         //        Intent chooser = Intent.createChooser(intent, "Select File");
//         //        cordova.startActivityForResult(this, chooser, PICK_PDF_FILE);

//         //#####################


//             Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//             intent.setType(uri_filter);
//             intent.addCategory(Intent.CATEGORY_OPENABLE);
//             intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

//             Intent chooser = Intent.createChooser(intent, "Escolher Mapa");
//             cordova.startActivityForResult(this, chooser, PICK_FILE_REQUEST);
//         }


    @PluginMethod
    public void pickFiles(PluginCall call) {
        try {
            JSArray types = call.getArray("types", null);
            boolean multiple = call.getBoolean("multiple", false);
            String[] parsedTypes = parseTypesOption(types);
            dataToSave = null;

            //Log.d(TAG, "pickFiles: parsedTypes "+ parsedTypes[0] + " / " + parsedTypes.length);
            if( parsedTypes != null && parsedTypes.length == 2 && parsedTypes[0].equals("save"))  {
                String writeData = call.getString("writeData");
                Log.d("TESTE", "============TESTE=================  " + parsedTypes[0] + "  rwite: " + writeData);
                dataToSave = writeData;
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                
                // intent.setType("application/x-freemind");//application/vnd.ms-project

                // intent.setType("text/plain");//application/vnd.ms-project
                intent.setType(parsedTypes[1]);//application/vnd.ms-project
                intent.putExtra(Intent.EXTRA_TITLE, "");
                Intent chooser = Intent.createChooser(intent, "Salvar Mapa");
                startActivityForResult(call, intent, "pickFilesResult");

            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiple);
                if (multiple == false && parsedTypes != null && parsedTypes.length > 0) {
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, parsedTypes);
                }
                startActivityForResult(call, intent, "pickFilesResult");
            }
            
            



        } catch (Exception ex) {
            String message = ex.getMessage();
            Log.e(TAG, message);
            call.reject(message);
        }
    }

    @PluginMethod
    public void pickImages(PluginCall call) {
        try {
            boolean multiple = call.getBoolean("multiple", false);

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiple);
            intent.setType("image/*");
            intent.putExtra("multi-pick", multiple);
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] { "image/*" });

            startActivityForResult(call, intent, "pickFilesResult");
        } catch (Exception ex) {
            String message = ex.getMessage();
            Log.e(TAG, message);
            call.reject(message);
        }
    }

    @PluginMethod
    public void pickMedia(PluginCall call) {
        try {
            boolean multiple = call.getBoolean("multiple", false);

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiple);
            intent.setType("*/*");
            intent.putExtra("multi-pick", multiple);
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] { "image/*", "video/*" });

            startActivityForResult(call, intent, "pickFilesResult");
        } catch (Exception ex) {
            String message = ex.getMessage();
            Log.e(TAG, message);
            call.reject(message);
        }
    }

    @PluginMethod
    public void pickVideos(PluginCall call) {
        try {
            boolean multiple = call.getBoolean("multiple", false);

            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, multiple);
            intent.setType("video/*");
            intent.putExtra("multi-pick", multiple);
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] { "video/*" });

            startActivityForResult(call, intent, "pickFilesResult");
        } catch (Exception ex) {
            String message = ex.getMessage();
            Log.e(TAG, message);
            call.reject(message);
        }
    }

    @Nullable
    private String[] parseTypesOption(@Nullable JSArray types) {
        if (types == null) {
            return null;
        }
        try {
            List<String> typesList = types.toList();
            if (typesList.contains("text/csv")) {
                typesList.add("text/comma-separated-values");
            }
            return typesList.toArray(new String[0]);
        } catch (JSONException exception) {
            Logger.error("parseTypesOption failed.", exception);
            return null;
        }
    }

    @ActivityCallback
    private void pickFilesResult(PluginCall call, ActivityResult result) {
        try {
            if (call == null) {
                return;
            }
            boolean readData = call.getBoolean("readData", false);
            int resultCode = result.getResultCode();
            switch (resultCode) {
                case Activity.RESULT_OK:
                    JSObject callResult = createPickFilesResult(result.getData(), readData);
                    call.resolve(callResult);
                    break;
                case Activity.RESULT_CANCELED:
                    call.reject(ERROR_PICK_FILE_CANCELED);
                    break;
                default:
                    call.reject(ERROR_PICK_FILE_FAILED);
            }
        } catch (Exception ex) {
            String message = ex.getMessage();
            Log.e(TAG, message);
            call.reject(message);
        }
    }

    private JSObject createPickFilesResult(@Nullable Intent data, boolean readData) {
        JSObject callResult = new JSObject();
        List<JSObject> filesResultList = new ArrayList<>();
        if (data == null) {
            callResult.put("files", JSArray.from(filesResultList));
            return callResult;
        }
        List<Uri> uris = new ArrayList<>();
        if (data.getClipData() == null) {
            Uri uri = data.getData();
            uris.add(uri);
        } else {
            for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                Uri uri = data.getClipData().getItemAt(i).getUri();
                uris.add(uri);
            }
        }
        for (int i = 0; i < uris.size(); i++) {
            Uri uri = uris.get(i);
            if (uri == null) {
                continue;
            }

            
            Log.d("TESTE", "============TESTE=================  RESULT data:[" + dataToSave + "] uri:[" + uri + "]" );
            if( dataToSave != null ) {

                
                Log.d("TESTE", "=TESTE= Salvar DAta!" );
                
                Uri uriFile = uri;
                Log.w("TESTE", "AQUI9: ");
                Log.w("TESTE", uriFile.toString());                
                // callback.success(uriFile.toString());
                // Log.w("File URI:", file_uri);
                try {
                    //Uri uri2 = Uri.parse(uri);
                    
                    
                    // ParcelFileDescriptor pfd = this.cordova.getActivity().getContentResolver().
                    //         openFileDescriptor(uri, "w");
                    
                    
                    // getActivity().getApplicationContext().getContentResolver(). 
                    ParcelFileDescriptor pfd = getActivity().getContentResolver().
                            openFileDescriptor(uri, "w");
                    
                    
                    FileOutputStream fileOutputStream =
                            new FileOutputStream(pfd.getFileDescriptor());
                    // fileOutputStream.write(file_data.getBytes());
                    fileOutputStream.write(dataToSave.getBytes());
                    // Let the document provider know you're done by closing the stream.
                    fileOutputStream.close();
                    pfd.close();
                    //callback.success("Mapa salvo com sucesso!");
                    Log.w("TESTE", "Mapa salvo com sucesso!");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.w("TESTE", "ERRO FILE NOT FOUND!");
                    //callback.error("ERRO FILE NOT FOUND!");
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.w("TESTE", "ERRO EXCEPTION!");
                    //callback.error("ERRO EXCEPTION!");
                }
            }



            JSObject fileResult = new JSObject();
            if (readData) {
                fileResult.put("data", implementation.getDataFromUri(uri));
            }
            Long duration = implementation.getDurationFromUri(uri);
            if (duration != null) {
                fileResult.put("duration", duration);
            }
            FileResolution resolution = implementation.getHeightAndWidthFromUri(uri);
            if (resolution != null) {
                fileResult.put("height", resolution.height);
                fileResult.put("width", resolution.width);
            }
            fileResult.put("mimeType", implementation.getMimeTypeFromUri(uri));
            Long modifiedAt = implementation.getModifiedAtFromUri(uri);
            if (modifiedAt != null) {
                fileResult.put("modifiedAt", modifiedAt);
            }
            fileResult.put("name", implementation.getNameFromUri(uri));
            fileResult.put("path", implementation.getPathFromUri(uri));
            fileResult.put("size", implementation.getSizeFromUri(uri));
            fileResult.put("uri", uri);

            filesResultList.add(fileResult);
        }
        callResult.put("files", JSArray.from(filesResultList.toArray()));
        return callResult;
    }
}

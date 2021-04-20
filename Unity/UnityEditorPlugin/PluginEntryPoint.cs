using System;
using System.IO;
using UnityEditor;
using UnityEngine;

namespace UnityEditorPlugin
{
    [InitializeOnLoad]
    public static class PluginEntryPoint
    {
        private static bool ourEditorIsInitialized;
        
        static PluginEntryPoint()
        {
            Init();
        }

        public static void Init()
        {
            if (ourEditorIsInitialized) return;
            
            Debug.Log("Running Init()");

            if (ExternalEditorScriptLocator.HasCrayonScriptEditor())
            {
                var editorPath = ExternalEditorScriptLocator.LocateCrayonScriptEditor();
                Debug.Log($"Located CrayonScript Editor at {editorPath}");
                ExternalEditorPrefsWrapper.EnsureCrayonScriptIsInEditorsList(editorPath);
                if (ExternalEditorPrefsWrapper.IsCrayonScriptDefaultEditor())
                {
                    Debug.Log("Installing Crayon Script Editor");
                    ExternalEditorPrefsWrapper.ExternalScriptEditor = editorPath;
                    Debug.Log($"ExternalScriptEditor updated to {editorPath}");            
                }
            }

            ourEditorIsInitialized = true;
        }
    }

    public static class ExternalEditorPrefsWrapper
    {
        public static string ExternalScriptEditor
        {
            get => EditorPrefs.GetString("kScriptsDefaultApp");
            set => EditorPrefs.SetString("kScriptsDefaultApp", value);
        }
        
        public static bool IsCrayonScriptDefaultEditor()
        {
            //
            // for now we are using just IntelliJ Community default build
            // But later we will customize a CrayonScript IDE by building the source code from
            // https://github.com/CrayonScript/intellij-community
            // 

            var defaultEditor = ExternalScriptEditor;
            Debug.Log($"current defaultEditor={defaultEditor}");
            if (string.IsNullOrEmpty(defaultEditor)) { return false; }
            var isDefaultEditor = ContainsCrayonScriptEditorString(defaultEditor);
            Debug.Log($"CrayonScript is default editor is {isDefaultEditor}");
            return isDefaultEditor;
        }
        
        public static void EnsureCrayonScriptIsInEditorsList(string editorPath)
        {
            const string recentlyUsedKeyPrefix = "RecentlyUsedScriptApp";
            string recentlyUsedKey = null;
            // upto 10 
            for (var i = 0; i < 10; i++)
            {
                recentlyUsedKey = $"{recentlyUsedKeyPrefix}{i}";
                var path = EditorPrefs.GetString(recentlyUsedKey);
                if (ContainsCrayonScriptEditorString(path)) return;
            }
            recentlyUsedKey = $"{recentlyUsedKeyPrefix}{9}";
            Debug.Log("Adding Crayon Script Editor to List");
            EditorPrefs.SetString(recentlyUsedKey, editorPath);
        }

        private static bool ContainsCrayonScriptEditorString(string str)
        {
            if (string.IsNullOrEmpty(str)) { return false; }
            str = str.ToLower();
            const string strToSearchFor = "intellij idea";
            return (str.Contains(strToSearchFor)) ;
        }
    }

    public static class ExternalEditorScriptLocator
    {
        public static string LocateCrayonScriptEditor()
        {
            switch (SystemInfo.operatingSystemFamily)
            {
                case OperatingSystemFamily.Windows:
                {
                    return LocateEditorOnWindows();
                }

                default:
                {
                    throw new ArgumentException("unsupported OS");
                }
            }
        }
        
        public static bool HasCrayonScriptEditor()
        {
            switch (SystemInfo.operatingSystemFamily)
            {
                case OperatingSystemFamily.Windows:
                {
                    var editorPath = LocateEditorOnWindows();
                    Debug.Log($"Located CrayonScript editor at {editorPath}");
                    var fileExists = File.Exists(editorPath);
                    Debug.Log($"CrayonScript editor fileExists={fileExists}");
                    return fileExists;
                }

                default:
                {
                    return false;
                }
            }
        }
        
        private static string LocateEditorOnWindows()
        {
            var localAppData = Environment.GetFolderPath(Environment.SpecialFolder.LocalApplicationData);
            var intelliJPath = Path.Combine(localAppData, @"JetBrains/IdeaIC2021.1");
            var intelliJHomeFileName = Path.Combine(intelliJPath, ".home");
            var intelliJHomePath = File.ReadAllText(intelliJHomeFileName);
            var intelliJExePath = $"{intelliJHomePath}\\bin\\idea64.exe";
            return intelliJExePath;
        }
    }
}
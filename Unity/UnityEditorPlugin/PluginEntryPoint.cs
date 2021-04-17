using System.IO;
using UnityEditor;
using UnityEngine.Device;

namespace UnityEditorPlugin
{
    [InitializeOnLoad]
    public static class PluginEntryPoint
    {
        private static bool isInitialized;

        static PluginEntryPoint()
        {
            Init();
        }

        public static void Init()
        {
            if (isInitialized) return;

            var projectDirectory = Directory.GetParent(Application.dataPath).FullName;
            
            
            isInitialized = true;
        }
    }
}
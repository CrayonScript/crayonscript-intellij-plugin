using UnityEditor;
using UnityEditor.Callbacks;

using Application = UnityEngine.Application;
using Debug = UnityEngine.Debug;

namespace CrayonScript.Unity.Editor
{
    [InitializeOnLoad]
    public static class PluginEntryPoint
    {
        private static bool isInitialized;

        static PluginEntryPoint()
        {
            isInitialized = true;
        }
    }
}
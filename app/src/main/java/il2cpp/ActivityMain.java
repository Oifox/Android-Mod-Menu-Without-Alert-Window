package il2cpp;

import android.animation.*;
import android.app.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import il2cpp.typefaces.*;

import il2cpp.typefaces.Menu;
import android.view.View.*;
import il2cpp.*;

public class ActivityMain extends Activity  
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
		
        super.onCreate(savedInstanceState);
		Main.start(this);
    }
}

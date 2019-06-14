package com.android.oy.huarongroad;

import java.io.Serializable;

public class TipItem implements Serializable {
    private static final long serialVersionUID = -5809782578272943999L;
    public TipItem before;
    public int[] list;
    public int type;
    public int to;

    public TipItem(int[] newList, TipItem parent, int itype, int ito) {
        list = newList.clone();
        type = itype;
        to = ito;
        if (parent != null) {
            before = parent;
        }
    }

    @Override
    public boolean equals(Object obj) {
//        if (obj == this) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TipItem item = (TipItem) obj;
        if (list.length != item.list.length) return false;
        for (int i = 0; i < list.length; i++) {
            if (list[i] != item.list[i]) {
                return false;
            }
        }
        return true;
    }
}

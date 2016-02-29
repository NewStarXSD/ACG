package com.acg.image;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter.ViewBinder;

public class MyViewBinder implements ViewBinder
{
    /**
     * view��Ҫ�嶥���ݵ���ͼ
     * data��Ҫ�󶨵���ͼ������
     * textRepresentation��һ����ʾ��֧�����ݵİ�ȫ���ַ����������data.toString()����ַ�������������Null
     * ����ֵ��������ݰ󶨵���ͼ�����棬���򷵻ؼ�
     */
    @Override
    public boolean setViewValue(View view, Object data,
            String textRepresentation) {
        if((view instanceof ImageView)&(data instanceof Bitmap))
        {
            ImageView iv = (ImageView)view;
            Bitmap bmp = (Bitmap)data;
            iv.setImageBitmap(bmp);
            return true;
        }
        return false;
    }
    
    
}
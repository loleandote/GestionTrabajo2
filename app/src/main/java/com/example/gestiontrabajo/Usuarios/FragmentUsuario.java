package com.example.gestiontrabajo.Usuarios;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.gestiontrabajo.ActividadConUsuario;
import com.example.gestiontrabajo.Conexión.apiRoles;
import com.example.gestiontrabajo.Conexión.apiUsuarios;
import com.example.gestiontrabajo.DatePickerFragment;
import com.example.gestiontrabajo.Datos.Rol;
import com.example.gestiontrabajo.Datos.Usuario;
import com.example.gestiontrabajo.Observaciones.FragmentObservaciones;
import com.example.gestiontrabajo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentUsuario extends Fragment {

   public ActividadConUsuario actividadConUsuario;
   private View vista;
   private Usuario usuario;
    EditText nombreUsuario;
    EditText contraseñaUsuario;
    EditText correoUsuario;
    EditText monedoUsuario;
    Spinner RolUsuario;
    Switch EsClienteUsuario;
    Switch EsPenalizadoUsuario;
    EditText FinPenaUsuario;
    ConstraintLayout ConstraintPena;
    Button guardarUsuario;
    int dia, mes, anyo;
    FragmentUsuarios fragmentUsuarios;
    ArrayList<Rol>listaRolesDisponibles;
    FloatingActionButton IrAObservaciones;
    public FragmentUsuario() {
        // Required empty public constructor
    }

    public FragmentUsuario(ActividadConUsuario actividadConUsuario){
        this.actividadConUsuario= actividadConUsuario;
    }

    public FragmentUsuario(ActividadConUsuario actividadConUsuario, Usuario usuario) {
        this.actividadConUsuario = actividadConUsuario;
        this.usuario = usuario;
    }

    public FragmentUsuario(ActividadConUsuario actividadConUsuario,FragmentUsuarios fragmentUsuarios, Usuario usuario) {
        this.actividadConUsuario = actividadConUsuario;
        this.fragmentUsuarios= fragmentUsuarios;
        this.usuario = usuario;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_usuario, container, false);
        if (usuario==null)
            usuario= new Usuario();
        nombreUsuario= vista.findViewById(R.id.nombreUsuario);
        nombreUsuario.setText(usuario.getNombre_usuario());
        contraseñaUsuario = vista.findViewById(R.id.contraseñaEditText);
        contraseñaUsuario.setText(usuario.getContraseña_usuario());
        correoUsuario = vista.findViewById(R.id.correoUsuarioEditTexto);
        correoUsuario.setText(usuario.getCorreo_usuario());
        monedoUsuario= vista.findViewById(R.id.creditosUsuario);
        monedoUsuario.setText(String.valueOf(usuario.getCreditos()));
        RolUsuario = vista.findViewById(R.id.RolUsuario);
        obtenerRoles();

        Resources resources= getResources();
        EsClienteUsuario = vista.findViewById(R.id.EsClienteUsuario);
        EsClienteUsuario.setChecked(usuario.isEs_cliente());
        EsPenalizadoUsuario = vista.findViewById(R.id.EsPenalizadoUsuario);
        EsPenalizadoUsuario.setChecked(usuario.isPenalizado());
        EsPenalizadoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EsPenalizadoUsuario.isChecked()) {
                    if (!usuario.isPenalizado()){
                        Date fecha = new Date();
                        FinPenaUsuario.setText(String.valueOf(fecha.getDate())+"-"+String.valueOf(fecha.getMonth())+"-"+String.valueOf(fecha.getYear()+1900));
                    }
                    ConstraintPena.setVisibility(View.VISIBLE);
                }
                else
                    ConstraintPena.setVisibility(View.INVISIBLE);
            }
        });

        FinPenaUsuario = vista.findViewById(R.id.FinPenaUsuario);

        FinPenaUsuario.setText(String.valueOf(usuario.getDia_fin_pena())+"-"+String.valueOf(usuario.getMes_fin_pena())+"-"+String.valueOf(usuario.getAnyo_fin_pena()));
        FinPenaUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        ConstraintPena = vista.findViewById(R.id.ConstraintPena);
        if (!usuario.isPenalizado())
            ConstraintPena.setVisibility(View.INVISIBLE);

        guardarUsuario = vista.findViewById(R.id.GuardarUsuario);
        guardarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiUsuarios apiUsuarios = actividadConUsuario.retrofit.create(apiUsuarios.class);

                if(usuario.getId()>0) {
                    usuario.setNombre_usuario(String.valueOf(nombreUsuario.getText()));
                    usuario.setContraseña_usuario(String.valueOf(contraseñaUsuario.getText()));
                    usuario.setCorreo_usuario(String.valueOf(correoUsuario.getText()));
                    usuario.setCreditos(Integer.parseInt(String.valueOf(monedoUsuario.getText())));
                    Rol rolSeleccionado = listaRolesDisponibles.get(RolUsuario.getSelectedItemPosition());
                    usuario.setCodigo_rol(rolSeleccionado.getId());
                    usuario.setEs_cliente(EsClienteUsuario.isChecked());
                    usuario.setPenalizado(EsPenalizadoUsuario.isChecked());

                    if (usuario.isPenalizado()) {
                        usuario.setDia_fin_pena(dia);
                        usuario.setMes_fin_pena(mes);
                        usuario.setAnyo_fin_pena(anyo);
                    }
                    if(usuario.getNombre_usuario().length()>3&&usuario.getNombre_usuario().length()<9&&usuario.getContraseña_usuario().length()>7&& usuario.getContraseña_usuario().length()<17&&validarEmail(usuario.getCorreo_usuario()))
                    {
                        actividadConUsuario.mensajeCorrecto(vista, inflater, R.string.UsuarioCorrecto);
                        Call<Usuario> respuesta = apiUsuarios.actualizarUsuario(usuario.getId(), usuario);
                    respuesta.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            if (response.isSuccessful()) {
                                FragmentUsuarios fragmentUsuarios = new FragmentUsuarios(actividadConUsuario);
                                actividadConUsuario.cambiarFragmento(fragmentUsuarios, actividadConUsuario.getResources().getString(R.string.Usuarios));
                            }
                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });}

                }
                else{

                    usuario.setNombre_usuario(String.valueOf(nombreUsuario.getText()));
                    usuario.setContraseña_usuario(String.valueOf(contraseñaUsuario.getText()));
                    usuario.setCorreo_usuario(String.valueOf(correoUsuario.getText()));
                    usuario.setCreditos(Integer.parseInt(String.valueOf(monedoUsuario.getText())));
                    Rol rolSeleccionado = listaRolesDisponibles.get(RolUsuario.getSelectedItemPosition());
                    usuario.setCodigo_rol(rolSeleccionado.getId());
                    usuario.setEs_cliente(EsClienteUsuario.isChecked());
                    usuario.setPenalizado(EsPenalizadoUsuario.isChecked());

                    if (usuario.isPenalizado()) {
                        usuario.setDia_fin_pena(dia);
                        usuario.setMes_fin_pena(mes);
                        usuario.setAnyo_fin_pena(anyo);
                    }
                    Date hoy = new Date();
                    if(usuario.getNombre_usuario().length()>3&&usuario.getNombre_usuario().length()<9&&usuario.getContraseña_usuario().length()>7&& usuario.getContraseña_usuario().length()<17&&validarEmail(usuario.getCorreo_usuario())) {
                        Call<Usuario> respuesta = apiUsuarios.guardaUsuario(usuario.getNombre_usuario(), usuario.getContraseña_usuario(), usuario.getCorreo_usuario(), hoy.getDate(), hoy.getMonth(), hoy.getYear() + 1900, usuario.getCreditos(), usuario.isPenalizado(), 0, 0, 0, usuario.getCodigo_rol(), usuario.getNombre_rol());
                        respuesta.enqueue(new Callback<Usuario>() {
                            @Override
                            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                                if (response.isSuccessful()) {
                                    FragmentUsuarios fragmentUsuarios = new FragmentUsuarios(actividadConUsuario);
                                    actividadConUsuario.cambiarFragmento(fragmentUsuarios,  actividadConUsuario.getResources().getString(R.string.Usuarios));
                                }
                            }

                            @Override
                            public void onFailure(Call<Usuario> call, Throwable t) {
                                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    if(usuario.getNombre_usuario().length()>3&&usuario.getNombre_usuario().length()<9)
                        Toast.makeText(getActivity(), resources.getString(R.string.NombreIncorrecto), Toast.LENGTH_SHORT).show();
                    else if(usuario.getContraseña_usuario().length()>7&& usuario.getContraseña_usuario().length()<17)  Toast.makeText(getActivity(), resources.getString(R.string.ContraseñaIncorrecta), Toast.LENGTH_SHORT).show();
                    else  Toast.makeText(getActivity(), resources.getString(R.string.NombreIncorrecto), Toast.LENGTH_SHORT).show();
                }
            }
        });

        IrAObservaciones= vista.findViewById(R.id.IrAObservaciones);
        IrAObservaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentObservaciones fragmentObservaciones= new FragmentObservaciones(actividadConUsuario, usuario,false);
                actividadConUsuario.cambiarFragmento(fragmentObservaciones,  actividadConUsuario.getResources().getString(R.string.Observaciones));
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (actividadConUsuario.drawerLayout.isDrawerOpen(Gravity.LEFT))
                    actividadConUsuario.drawerLayout.closeDrawers();
                else {
                    if (fragmentUsuarios != null)
                        actividadConUsuario.cambiarFragmento(fragmentUsuarios, actividadConUsuario.getResources().getString(R.string.Usuarios));
                    else {
                        FragmentUsuarios fragmentUsuarios = new FragmentUsuarios(actividadConUsuario);
                        actividadConUsuario.cambiarFragmento(fragmentUsuarios, actividadConUsuario.getResources().getString(R.string.Usuarios));
                    }
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    return vista;
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + "-" + (month+1) + "-" + year;
                Date fechaHoy = new Date();
                Date fechaBan =new Date(year, month+1, day);
                if (fechaHoy.compareTo(fechaBan)==1)
                EsPenalizadoUsuario.setText(selectedDate);
                else {
                    String mensaje = getResources().getString(R.string.FechaIncorrecta);
                    Toast.makeText(getActivity(), mensaje, Toast.LENGTH_SHORT).show();

                }
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private  void obtenerRoles()
    {
        ArrayList<Integer>listaRoles=new ArrayList<>();
        for(int i=0; i<=actividadConUsuario.rol.getRango_rol();i++)
            listaRoles.add(i);
        apiRoles apiRoles = actividadConUsuario.retrofit.create(apiRoles.class);
        Call<ArrayList<Rol>>respuesta = apiRoles.obtenerRoles(listaRoles);
        respuesta.enqueue(new Callback<ArrayList<Rol>>() {
            @Override
            public void onResponse(Call<ArrayList<Rol>> call, Response<ArrayList<Rol>> response) {
                if(response.isSuccessful()){
                    listaRolesDisponibles= response.body();
                    System.out.println(response.body().size());
                    String []listaNombres = new String[response.body().size()];
                    for(int i=0; i<response.body().size();i++)
                        listaNombres[i]=response.body().get(i).getNombre_rol();
                    actualizarRoles(listaNombres);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Rol>> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void actualizarRoles(String[]listaRoles){
        ArrayAdapter<String> listaAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, listaRoles);
        RolUsuario.setAdapter(listaAdapter);
        RolUsuario.setSelection(usuario.getCodigo_rol()-1);
    }
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
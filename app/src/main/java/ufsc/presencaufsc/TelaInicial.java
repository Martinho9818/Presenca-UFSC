package ufsc.presencaufsc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ufsc.presencaufsc.adapter.DisciplinaAdapter;
import ufsc.presencaufsc.dao.DB;
import ufsc.presencaufsc.dao.DBLocal;
import ufsc.presencaufsc.dao.DBOpenHelper;
import ufsc.presencaufsc.model.Aluno;
import ufsc.presencaufsc.model.Aula;
import ufsc.presencaufsc.model.Disciplina;
import ufsc.presencaufsc.model.Instituicao;

public class TelaInicial extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CODE = 1000;
    private String dadosQr;
    private DB db = new DB();
    private SQLiteDatabase conexao;
    private DBOpenHelper dbOpenHelper;
    private DBLocal dbLocal;
    private Aluno aluno;
    private Instituicao instituicao;
    private Toast toast;
    private int qtdDadosSemSincronizacao;

    private Float latitude;
    private Float longitude;
    private Float latitude_min;
    private Float longitude_min;
    private Float latitude_max;
    private Float longitude_max;

    private Intent it;
    private RecyclerView lstDisciplinasTI;
    private DisciplinaAdapter disciplinaAdapter;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    /* Metodo para ver a permissão da localização */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_tela_inicial);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pegarLocalizao();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pegarLocalizao();
                chamarTelaQRCode();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        lstDisciplinasTI = (RecyclerView) findViewById(R.id.lstDisciplinasTI);
        criarConexao();
        try {
            lstDisciplinasTI.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            lstDisciplinasTI.setLayoutManager(linearLayoutManager);
            List<Disciplina> listaDisciplina = dbLocal.buscarTodasDisciplinas(dbLocal.buscarAlunoLocal().getCod_instituicao_cadastrada());
            disciplinaAdapter = new DisciplinaAdapter(listaDisciplina);
            lstDisciplinasTI.setAdapter(disciplinaAdapter);
        } catch (Exception e) {
            System.out.println(e);
        }

        tutorial();
        verificaDadosNaoSincronizados();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_aulas) {

        } else if (id == R.id.nav_disciplina) {
            it = new Intent(TelaInicial.this, TelaDisciplinas.class);
            startActivity(it);
        } else if (id == R.id.nav_aluno) {
            if (dbLocal.verificaAlunoLocal() == true) {
                it = new Intent(TelaInicial.this, TelaInformacoesAluno.class);
                startActivity(it);
            } else {
                it = new Intent(TelaInicial.this, TelaCadastroAluno.class);
                startActivity(it);
            }
        } else if (id == R.id.nav_recuperacao_conta) {
            if (dbLocal.verificaAlunoLocal() == true) {
                alerta("Aluno ja cadastrado");
            } else {
                it = new Intent(TelaInicial.this, TelaRecuperacaoConta.class);
                startActivity(it);
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /* Recupera as informações do Leitor de QRcode */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() != null) {
                //alerta(result.getContents());
                salvarPresenca(result.getContents());
            } else {
                alerta(getString(R.string.scan_cancelado));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tela_inicial, menu);
        return true;
    }

    /* Menu de sincronizacao na tela inicial */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sync) {
            if (dbLocal.verificaAlunoLocal() == false) {
                tutorial();
            } else {
                /* O método de sincronização pode demorar um pouco caso o usuário tenha muitas aulas/disciplinas */
                sincronizacaoPresencaDBs();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* ________________________________ Conexão _____________________________________*/
    /* Método responsável por criar a conexão com o banco local */
    private void criarConexao() {
        try {
            dbOpenHelper = new DBOpenHelper(this);
            conexao = dbOpenHelper.getWritableDatabase();
            dbLocal = new DBLocal(conexao);
        } catch (SQLException ex) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle("Erro");
            dlg.setMessage(ex.getMessage());
            dlg.setNeutralButton("Ok", null);
            dlg.show();
        }
    }

    /* Método de alerta para o usuário (apenas para facilitar a comunicacao) */
    private void alerta(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /* ________________________________ QRCode ______________________________________*/
    public void chamarTelaQRCode() {
        /* Verifica se o gps ta ligado com base na latitude e longitude */
        if (latitude != null && longitude != null) {
            /* Parte para chamar o Leitor de QRcode */
            IntentIntegrator intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            intentIntegrator.setPrompt("Aproxime o Código QR");
            intentIntegrator.setCameraId(0);
            intentIntegrator.initiateScan();
        } else {
            Toast.makeText(getApplicationContext(), R.string.gpsDesligadoLigue, Toast.LENGTH_SHORT).show();
        }
    }

    /* Metodo para salvar a presenca (verificando localizacao, aluno, pegando data e info. QRCode */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void salvarPresenca(String dadosQr) {
        this.dadosQr = dadosQr;
        Aula aula = new Aula();
        aula.setDataAula(pegaDataAtual());
        aula.setHoraAula(pegaHoraAtual());
        aula.setCod_disciplina(dadosQr);
        if (!retornaLocalizacao().equals(null)) {
            aula.setLocalizacaoAluno(retornaLocalizacao());
            aula.setLocalizacaoNoPerimetro(verificaPerimetroInstituicao());
            if (dbLocal.verificaAlunoLocal() == true) {
                this.aluno = dbLocal.buscarAlunoLocal();
                aula.setCod_aluno(aluno.getMatricula());
                /* Tentando salvar no banco de dados externo e verificando */
                try {
                    db.salvarAula(aula);
                    if (db.verificarAula(aula.getHoraAula(), aluno.getMatricula()) == true) {
                        alerta(getString(R.string.presenca_salva));
                    } else {
                        try {
                            dbLocal.atualizaDadosPendentes(1);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        Toast.makeText(getApplicationContext(),
                                R.string.sem_rede_sincronize_no_botao,
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                /* Tentando salvar no banco de dados interno */
                try {
                    dbLocal.salvarPresencaLocal(aula);
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            } else {
                alerta("Erro, aluno ainda não cadastrado.");
                alerta("Por favor, faça o cadastro na aba 'Aluno'");
            }
        }
    }

    /* ________________________________ Data _______________________________________*/
    /* Metodo pegar a data atual */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private String pegaDataAtual() {
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        String data_completa = dateFormat.format(data_atual);
        return data_completa;
    }

    /* Metodo pegar a data em formato de hora atual */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private String pegaHoraAtual() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        String data_completa = dateFormat.format(data_atual);
        return data_completa;
    }

    /* Método responsável por avisar se o aluno não esta cadastrado. E disciplina */
    public void tutorial() {
        if (dbLocal.verificaAlunoLocal() == true) {
            if (dbLocal.verificaDisciplinaLocal() != true) {
                Toast.makeText(getApplicationContext(),
                        R.string.tutorial_disciplina,
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    R.string.tutorialAlunoNaoCadastrado,
                    Toast.LENGTH_LONG).show();
        }
    }

    /* ________________________________ Localizacao ________________________________*/
    /* Método Localização */
    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    //System.out.println("Latitude: " + String.valueOf(location.getLatitude()) + " Longitude:" + String.valueOf(location.getLongitude()));
                    latitude = Float.valueOf(String.valueOf(location.getLatitude()));
                    longitude = Float.valueOf(String.valueOf(location.getLongitude()));
                }
            }
        };
    }

    /* Método Localização */
    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
    }

    /* Método que requisita a localização do usuário */
    public void pegarLocalizao() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            /* Se a permissão for garantida: */
            buildLocationRequest();
            buildLocationCallBack();
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
//        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    /* Método de recuperação dos dados da instituição local */
    public boolean retornarDadosLocalizacaoInstituicao() {
        Boolean confirmacao = false;
        try {
            instituicao = dbLocal.buscarInstituicaoLocal();
            latitude_min = Float.valueOf(instituicao.getLatitude_min());
            longitude_min = Float.valueOf(instituicao.getLongitude_min());
            latitude_max = Float.valueOf(instituicao.getLatitude_max());
            longitude_max = Float.valueOf(instituicao.getLongitude_max());
            confirmacao = true;
            return confirmacao;
        } catch (Exception e) {
            System.out.println(e);
        }
        return confirmacao;
    }

    /* Método que verifica se a localização do aluno esta dentro do raio da instituição de ensino */
    public int verificaPerimetroInstituicao() {
        Integer confirmacao = 0;
        if (retornarDadosLocalizacaoInstituicao() == true) {
            if ((longitude > longitude_max && longitude < longitude_min) && (latitude > latitude_max && latitude < latitude_min)) {
                confirmacao = 1;
                return confirmacao;
            } else {
                return confirmacao;
            }
        } else {
            //System.out.println("Não foi possivel pegar os dados da Instituicao");
        }
        return confirmacao;
    }

    public String retornaLocalizacao() {
        StringBuilder localizacaoAluno = new StringBuilder();
        localizacaoAluno.append("Latitude: ");
        localizacaoAluno.append(latitude);
        localizacaoAluno.append(" Longitude: ");
        localizacaoAluno.append(longitude);
        return localizacaoAluno.toString();
    }

    /* _______________________________ Sincronização ________________________________*/
    public void sincronizacaoPresencaDBs() {
        try {
            Aluno aluno1 = dbLocal.buscarAlunoLocal();
            /* Se o aluno existir, sincroniza, caso não, avisa o usuário para cadastrar */
            if (aluno1 != null && !aluno1.getMatricula().equals(null)) {
                List<Disciplina> listaDisciplinas = dbLocal.buscarTodasDisciplinas(aluno1.getCod_instituicao_cadastrada());

                if (listaDisciplinas.size() > 0 && listaDisciplinas != null) {
                    List<Aula> listaAulas = null;
                    int i = 0;
                    for (i = 0; i < listaDisciplinas.size(); i++) {
                        listaAulas = dbLocal.buscarPresencaDaDisciplina(aluno1.getMatricula(), listaDisciplinas.get(i).getCodigoDisciplina());

                        /* ---------------- enviando para o banco de dados externo ---------------- */
                        int count = 0;
                        for (count = 0; count < listaAulas.size(); count++) {
                            if (db.verificarAula(listaAulas.get(count).getHoraAula(), aluno1.getMatricula()) == false) {
                                db.salvarAula(listaAulas.get(count));
                                //System.out.println("@---- Aula não cadastrada, cadastrando... ----@");
                            } else {
                                //System.out.println("Aula ja cadastrada no banco, pulando...");
                            }
                        }
                        //System.out.println("Terminou a verificação.");
                    }
                }
                try {
                    dbLocal.atualizaDadosPendentes(0);
                    Toast.makeText(getApplicationContext(),
                            R.string.sincronizacao_realizada_sucesso,
                            Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        } catch (Exception e) {
            tutorial();
            System.out.println(e);
        }
    }

    /* Método responsável por verificar se há alguma presença no dispositivo que não esteja no sistema (DB externo) */
    public void verificaDadosNaoSincronizados() {
        try {
            qtdDadosSemSincronizacao = dbLocal.buscarDadosPendentes();
            if (qtdDadosSemSincronizacao <= 0) {
                //System.out.println("Sem dados pendentes");
            } else {
                Toast.makeText(getApplicationContext(),
                        R.string.voce_tem_dados_pendentes,
                        Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),
                        R.string.instrucoes_sincronizacao,
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}

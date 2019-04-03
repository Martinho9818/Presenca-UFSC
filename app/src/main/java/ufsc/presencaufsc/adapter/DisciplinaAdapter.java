package ufsc.presencaufsc.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ufsc.presencaufsc.R;
import ufsc.presencaufsc.TelaPresenca;
import ufsc.presencaufsc.model.Disciplina;

public class DisciplinaAdapter extends RecyclerView.Adapter<DisciplinaAdapter.ViewHolderDisciplina> {

    private List<Disciplina> dadoDisciplinas;

    public DisciplinaAdapter(List<Disciplina> pDadosDisciplinas) {
        this.dadoDisciplinas = pDadosDisciplinas;
    }

    @NonNull
    @Override
    public DisciplinaAdapter.ViewHolderDisciplina onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.linha_disciplina, parent, false);
        ViewHolderDisciplina holderDisciplina = new ViewHolderDisciplina(view, parent.getContext());
        return holderDisciplina;
    }

    @Override
    public void onBindViewHolder(@NonNull DisciplinaAdapter.ViewHolderDisciplina holder, int position) {
        if ((dadoDisciplinas != null) && (dadoDisciplinas.size() > 0)) {
            Disciplina disciplina = dadoDisciplinas.get(position);
            holder.txtNomeDisciplina.setText(disciplina.getNomeDisciplina());
            holder.txtCodInstituicao.setText(new StringBuilder().append("Código da Instituição: ").append(disciplina.getCodInstituicaoDisciplina().toString()));
            holder.txtCodigoDiscplina.setText(new StringBuilder().append("Código da Disciplina: ").append(disciplina.getCodigoDisciplina()).toString());
            holder.txtProfessorDisciplina.setText(new StringBuilder().append("Professor: ").append(disciplina.getProfessor()).toString());

        }
    }

    @Override
    public int getItemCount() {
        return dadoDisciplinas.size();
    }

    public class ViewHolderDisciplina extends RecyclerView.ViewHolder {

        public TextView txtNomeDisciplina;
        public TextView txtCodInstituicao;
        public TextView txtCodigoDiscplina;
        public TextView txtProfessorDisciplina;

        public ViewHolderDisciplina(View itemView, final Context context) {
            super(itemView);
            txtNomeDisciplina = (TextView) itemView.findViewById(R.id.textViewNomeDisciplina);
            txtCodInstituicao = (TextView) itemView.findViewById(R.id.textViewCodInstituicaoDisciplina);
            txtCodigoDiscplina = (TextView) itemView.findViewById(R.id.textViewCodigoDisciplina);
            txtProfessorDisciplina = (TextView) itemView.findViewById(R.id.textViewProfessorDisciplina);

            /* Quando o usuário clicar chama a tela de presenca */
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (dadoDisciplinas.size() > 0) {
                        /* Pega a posição do Recicled View e depois passa (se houver) o dado pelo bundle */
                        Disciplina disciplina = dadoDisciplinas.get(getLayoutPosition());
                        /* Chama a tela de presenca ao clicar */
                        Intent it = new Intent(context, TelaPresenca.class);
                        /* Passando o a chave para pegar o classe pelo bundle */
                        it.putExtra("DISCIPLINA", disciplina);

                        ((AppCompatActivity) context).startActivityForResult(it, 0);
                    }
                }
            });
        }
    }
}

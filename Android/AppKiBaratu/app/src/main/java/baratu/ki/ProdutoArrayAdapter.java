package baratu.ki;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProdutoArrayAdapter extends ArrayAdapter<Produto> {

    private List<Produto> produtoList = new ArrayList<Produto>();

    private View row;

    static class ProdutoViewHolder {
        TextView nome;
        TextView marca;
        TextView preco;
        TextView supermercadoNome;
    }

    //Constructor of class
    public ProdutoArrayAdapter(Context context, int resourceId) {
        super(context, resourceId);
    }

    @Override
    public void clear()
    {
        super.clear();
        produtoList.clear();
    }

    @Override
    public void add(Produto object) {
        super.add(object);
        produtoList.add(object);
    }

    @Override
    public int getCount() {
        return this.produtoList.size();
    }

    @Override
    public Produto getItem(int index) {
        return this.produtoList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ProdutoViewHolder viewHolder;

        row = convertView;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.layout_listview_produto, parent, false);

            viewHolder = new ProdutoViewHolder();

            viewHolder.nome = (TextView) row.findViewById(R.id.txtNome);
            viewHolder.marca = (TextView) row.findViewById(R.id.txtMarca);
            viewHolder.preco = (TextView) row.findViewById(R.id.txtPreco);
            viewHolder.supermercadoNome = (TextView) row.findViewById(R.id.txtSupermercado);

            row.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ProdutoViewHolder)row.getTag();
        }

        //Paint  list color
        setRowColor(position);

        Produto produto = getItem(position);

        viewHolder.nome.setText("Produto: " + produto.getNome());
        viewHolder.marca.setText("Marca: " + produto.getMarca());
        viewHolder.preco.setText("Preço: " + produto.getPreco());
        viewHolder.supermercadoNome.setText("Supermercado: " + produto.getSupermercadoNome());

        return row;
    }

    private void setRowColor(int position)
    {
		if(position % 2 == 1)
		{
			row.setBackgroundResource(R.color.fontAzulClaro);
		}
    }
}